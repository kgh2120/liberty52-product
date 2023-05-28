package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.badrequest.AbnormalOrderStatusRequestException;
import com.liberty52.product.global.exception.external.badrequest.OrderStatusIsNotWaitingDepositException;
import com.liberty52.product.global.exception.external.badrequest.SameOrderStatusRequestException;
import com.liberty52.product.global.exception.external.notfound.OrderNotFoundByIdException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.applicationservice.OrderStatusModifyService;
import com.liberty52.product.service.controller.dto.VBankStatusModifyDto;
import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.payment.Payment;
import com.liberty52.product.service.entity.payment.VBankPayment;
import com.liberty52.product.service.entity.payment.VBankPayment.VBankPaymentInfo;
import com.liberty52.product.service.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderStatusModifyServiceImpl implements OrderStatusModifyService {
  private final OrdersRepository ordersRepository;
  @Override
  public void modifyOrderStatusByAdmin(String role, String orderId, OrderStatus requestedStatus) {
    Validator.isAdmin(role);
    Orders order = ordersRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundByIdException(orderId));

    OrderStatus currentStatus = order.getOrderStatus();
    OrderStatus previousStatus = OrderStatus.getPreviousStatus(requestedStatus);

    validateOrderStatus(requestedStatus, currentStatus, previousStatus);
    order.modifyOrderStatus(requestedStatus);
  }

  private void validateOrderStatus(OrderStatus requestedStatus, OrderStatus currentStatus,
      OrderStatus previousStatus) {
    if (currentStatus == requestedStatus) {
      throw new SameOrderStatusRequestException();
    }
    if (requestedStatus == OrderStatus.ORDERED || currentStatus != previousStatus) {
      throw new AbnormalOrderStatusRequestException();
    }
  }

  @Override
  public void modifyOrderStatusOfVBankByAdmin(String role, String orderId, VBankStatusModifyDto dto) {
    Validator.isAdmin(role);
    Orders order = ordersRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundByIdException(orderId));

    if (order.getOrderStatus() != OrderStatus.WAITING_DEPOSIT) {
      throw new OrderStatusIsNotWaitingDepositException();
    }

    VBankPayment.VBankPaymentInfo prev = order.getPayment().getInfoAsDto();
    VBankPaymentInfo vBank = VBankPaymentInfo.ofPaid(prev, dto);
    Payment<?> payment = order.getPayment();
    payment.setInfo(vBank);
    order.changeOrderStatusToOrdered();
  }
}
