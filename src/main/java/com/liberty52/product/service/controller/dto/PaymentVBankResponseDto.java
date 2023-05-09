package com.liberty52.product.service.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentVBankResponseDto {

    private String orderId;
    private String orderNum;

    public static PaymentVBankResponseDto of(String orderId, String orderNum) {
        return new PaymentVBankResponseDto(orderId, orderNum);
    }

}
