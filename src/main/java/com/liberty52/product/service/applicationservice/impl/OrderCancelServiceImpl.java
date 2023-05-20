package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.adapter.portone.PortOneService;
import com.liberty52.product.global.event.Events;
import com.liberty52.product.global.event.events.OrderCancelRequestedEvent;
import com.liberty52.product.global.event.events.OrderCanceledEvent;
import com.liberty52.product.global.exception.external.badrequest.AlreadyCancelOrderException;
import com.liberty52.product.global.exception.external.badrequest.CannotOrderCancelException;
import com.liberty52.product.global.exception.external.forbidden.NotYourOrderException;
import com.liberty52.product.global.exception.external.internalservererror.InternalServerErrorException;
import com.liberty52.product.global.exception.external.notfound.OrderNotFoundByIdException;
import com.liberty52.product.service.applicationservice.OrderCancelService;
import com.liberty52.product.service.controller.dto.OrderCancelDto;
import com.liberty52.product.service.entity.CanceledOrders;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.payment.Payment;
import com.liberty52.product.service.entity.payment.VBankPayment;
import com.liberty52.product.service.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderCancelServiceImpl implements OrderCancelService {

    @Value("${liberty52.mail.address.order-team}")
    private String ORDER_TEAM_MAIL_ADDRESS;
    private final PortOneService portOneService;
    private final OrdersRepository ordersRepository;
    private final String MSG_ORDER_CANCELED = "주문이 취소되었습니다.";
    private final String MSG_ORDER_CANCEL_REQUESTED = "주문 취소가 요청되었습니다.";

    @Override
    public OrderCancelDto.Response cancelOrder(String authId, OrderCancelDto.Request request) {
        Orders order = validateAndGetOrder(authId, request.getOrderId());

        return switch (order.getPayment().getType()) {
            case CARD -> cancelCardOrder(order, request);
            case VBANK -> cancelVbankOrder(order, request);
            default -> throw new InternalServerErrorException("주문의 결제 타입이 올바르지 않습니다. 관리자에게 문의해주세요.");
        };
    }

    private OrderCancelDto.Response cancelCardOrder(Orders order, OrderCancelDto.Request request) {
        CanceledOrders canceledOrders = CanceledOrders.of(request.getReason(), order);

        String message = switch (order.getOrderStatus()) {
            case ORDERED -> {
                portOneService.requestCancelPayment(order.getId(), request.getReason());
                canceledOrders.approveCanceled(0);
                order.changeOrderStatusToCanceled();

                Events.raise(OrderCanceledEvent.toCustomer(order));
                Events.raise(OrderCanceledEvent.toAdmin(order, ORDER_TEAM_MAIL_ADDRESS));
                yield MSG_ORDER_CANCELED;
            }
            case CANCELED, CANCEL_REQUESTED -> throw new AlreadyCancelOrderException();
            default -> throw new CannotOrderCancelException(order.getOrderStatus());
        };

        return OrderCancelDto.Response.of(message);
    }

    private OrderCancelDto.Response cancelVbankOrder(Orders order, OrderCancelDto.Request request) {
        CanceledOrders canceledOrder = CanceledOrders.of(request.getReason(), order);

        String message = switch (order.getOrderStatus()) {
            case WAITING_DEPOSIT -> {
                canceledOrder.approveCanceled(0);
                order.changeOrderStatusToCanceled();

                Events.raise(OrderCanceledEvent.toCustomer(order));
                Events.raise(OrderCanceledEvent.toAdmin(order, ORDER_TEAM_MAIL_ADDRESS));
                yield MSG_ORDER_CANCELED;
            }
            case ORDERED -> {
                VBankPayment.VBankPaymentInfo prev = order.getPayment().getInfoAsDto();
                VBankPayment.VBankPaymentInfo refund = VBankPayment.VBankPaymentInfo.refund(prev, request.getRefundVO());
                Payment<?> payment = order.getPayment();
                payment.setInfo(refund);
                order.changeOrderStatusToCancelRequest();

                Events.raise(OrderCancelRequestedEvent.toAdmin(order, ORDER_TEAM_MAIL_ADDRESS));
                yield MSG_ORDER_CANCEL_REQUESTED;
            }
            case CANCELED, CANCEL_REQUESTED -> throw new AlreadyCancelOrderException();
            default -> throw new CannotOrderCancelException(order.getOrderStatus());
        };

        return OrderCancelDto.Response.of(message);
    }

    private Orders validateAndGetOrder(String authId, String orderId) {
        return ordersRepository.findById(orderId)
                .filter(order -> {
                    if (!Objects.equals(order.getAuthId(), authId)) {
                        throw new NotYourOrderException(authId);
                    }
                    return true;
                })
                .orElseThrow(() -> new OrderNotFoundByIdException(orderId));
    }

}
