package com.liberty52.product.service.utils;

import com.liberty52.product.service.controller.dto.OrderCancelDto;
import com.liberty52.product.service.controller.dto.OrderRefundDto;

public class TestDtoBuilder {

    public static OrderCancelDto.Request orderCancelRequestDto(
            String orderId, String reason, String refundBank, String refundHolder, String refundAccount, String refundPhoneNum) {
        return OrderCancelDto.Request.builder()
                .orderId(orderId)
                .reason(reason)
                .refundBank(refundBank)
                .refundHolder(refundHolder)
                .refundAccount(refundAccount)
                .refundPhoneNum(refundPhoneNum)
                .build();
    }

    public static OrderRefundDto.Request orderRefundRequestDto(String orderId, Integer fee) {
        return OrderRefundDto.Request.builder()
                .orderId(orderId)
                .fee(fee)
                .build();
    }

}
