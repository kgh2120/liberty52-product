package com.liberty52.product.global.adapter.portone.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PortOneTokenRequestDto {

    private String imp_key;
    private String imp_secret;

    public static PortOneTokenRequestDto of(String impKey, String impSecret) {
        return new PortOneTokenRequestDto(impKey, impSecret);
    }
}
