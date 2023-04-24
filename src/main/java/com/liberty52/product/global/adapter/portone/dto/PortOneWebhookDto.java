package com.liberty52.product.global.adapter.portone.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PortOneWebhookDto {

    private String imp_uid;
    private String merchant_uid;
    private String status;

    public static PortOneWebhookDto testOf(String merchantUid, String status) {
        return new PortOneWebhookDto("IMP_TEST", merchantUid, status);
    }

}
