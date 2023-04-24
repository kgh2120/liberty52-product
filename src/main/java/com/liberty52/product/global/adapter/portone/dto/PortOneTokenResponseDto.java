package com.liberty52.product.global.adapter.portone.dto;

import lombok.Getter;

@Getter
public class PortOneTokenResponseDto {

    private int code;
    private String message;
    private PortOneTokenResponse response;

    @Getter
    public class PortOneTokenResponse {

        private String access_token;
        private Long now;
        private Long expired_at;

    }
}
