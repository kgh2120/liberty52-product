package com.liberty52.product.global.adapter.portone.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PortOneTokenDto {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {
        private String imp_key;
        private String imp_secret;

        public static PortOneTokenDto.Request of(String impKey, String impSecret) {
            return new PortOneTokenDto.Request(impKey, impSecret);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        private int code;
        private String message;
        private ApiResponse response;

        @Getter
        public class ApiResponse {

            private String access_token;
            private Long now;
            private Long expired_at;

        }
    }
}
