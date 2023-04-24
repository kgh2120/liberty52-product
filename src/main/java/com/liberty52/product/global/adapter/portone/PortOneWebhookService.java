package com.liberty52.product.global.adapter.portone;

import com.liberty52.product.global.adapter.portone.dto.PortOneWebhookDto;

public interface PortOneWebhookService {

    void hookPortOnePaymentInfo(PortOneWebhookDto dto);

    void hookPortOnePaymentInfoForTest(PortOneWebhookDto dto, Long amount);

}
