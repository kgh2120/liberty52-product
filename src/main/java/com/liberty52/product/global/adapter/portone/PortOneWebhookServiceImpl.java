package com.liberty52.product.global.adapter.portone;

import com.liberty52.product.global.adapter.portone.dto.PortOnePaymentInfo;
import com.liberty52.product.global.adapter.portone.dto.PortOneToken;
import com.liberty52.product.global.adapter.portone.dto.PortOneWebhookDto;
import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.payment.CardPayment;
import com.liberty52.product.service.repository.ConfirmPaymentMapRepository;
import com.liberty52.product.service.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class PortOneWebhookServiceImpl implements PortOneWebhookService {

    private final OrdersRepository ordersRepository;
    private final PortOneRequestClient portOneRequestClient;
    private final ConfirmPaymentMapRepository confirmPaymentMapRepository;

    @Override
    public void hookPortOnePaymentInfo(PortOneWebhookDto dto) {
        if (dto.getStatus().equals("paid")) {
            PortOneToken token = getAccessToken();

            PortOnePaymentInfo paymentInfo = getPaymentInfo(token.getAccessToken(), dto.getImp_uid());

            validateAmountAndSave(dto, paymentInfo);
        }
    }

    @Override
    public void hookPortOnePaymentInfoForTest(PortOneWebhookDto dto, Long amount) {
        testForhook(dto, "Liberty 52_Frame", amount, "");
    }

    public void testForhook(PortOneWebhookDto dto, String pName, Long amount, String authId) {
        PortOnePaymentInfo paymentInfo = PortOnePaymentInfo.testOf(dto.getMerchant_uid(), pName, amount, authId);
        validateAmountAndSave(dto, paymentInfo);
    }

    public void validateAmountAndSave(PortOneWebhookDto dto, PortOnePaymentInfo paymentInfo) {
        Orders order = ordersRepository.findById(dto.getMerchant_uid())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", dto.getMerchant_uid()));
        log.info("GET WEBHOOK DATA - Order ID: {}", order.getId());

        if (Objects.equals(order.getAmount(), paymentInfo.getAmount())) {
            order.changeOrderStatusToOrdered();
            order.getPayment().changeStatusToPaid();
            order.getPayment().setInfo(CardPayment.CardPaymentInfo.of(paymentInfo));
        } else {
            order.getPayment().changeStatusToForgery();
            log.info("누군가가 위조된 결제를 요청하였습니다. OrderId: {}, CustomerId: {}, Order Amount: {}, Paid Amount: {}",
                    order.getId(), paymentInfo.getCustomer_uid(), order.getAmount(), paymentInfo.getAmount());
        }

        confirmPaymentMapRepository.put(order.getId(), order);
    }

    private PortOneToken getAccessToken() {
        return portOneRequestClient.getAccessToken();
    }

    private PortOnePaymentInfo getPaymentInfo(String token, String impUid) {
        return portOneRequestClient.getPaymentInfo(token, impUid);
    }
}
