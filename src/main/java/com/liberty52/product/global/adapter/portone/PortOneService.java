package com.liberty52.product.global.adapter.portone;

import com.liberty52.product.global.adapter.portone.dto.PortOneWebhookDto;

public interface PortOneService {

    void hookPortOnePaymentInfo(PortOneWebhookDto dto);

    void hookPortOnePaymentInfoForTest(PortOneWebhookDto dto, Long amount);

    void requestCancelPayment(String orderId, String reason);

}
