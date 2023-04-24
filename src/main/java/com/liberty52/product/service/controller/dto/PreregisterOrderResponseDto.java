package com.liberty52.product.service.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PreregisterOrderResponseDto {

    private String merchantId;
    private Long amount;

    public static PreregisterOrderResponseDto of(String merchantId, Long amount) {
        return new PreregisterOrderResponseDto(merchantId, amount);
    }

}
