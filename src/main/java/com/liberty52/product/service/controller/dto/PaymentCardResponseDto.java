package com.liberty52.product.service.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentCardResponseDto {

    private String merchantId;
    private String orderNum;
    private Long amount;

    public static PaymentCardResponseDto of(String merchantId, String orderNum, Long amount) {
        return new PaymentCardResponseDto(merchantId, orderNum, amount);
    }

}
