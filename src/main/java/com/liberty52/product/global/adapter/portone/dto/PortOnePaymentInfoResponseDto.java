package com.liberty52.product.global.adapter.portone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PortOnePaymentInfoResponseDto {

    private int code;
    private String message;
    private PortOnePaymentInfo response;

}
