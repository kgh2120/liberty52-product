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

    public static PaymentVBankResponseDto of(String orderId) {
        return new PaymentVBankResponseDto(orderId);
    }

}
