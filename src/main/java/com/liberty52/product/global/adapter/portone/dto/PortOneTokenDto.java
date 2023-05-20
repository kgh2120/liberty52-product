package com.liberty52.product.global.adapter.portone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PortOneTokenDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String imp_key;
        private String imp_secret;

        public static PortOneTokenDto.Request of(String impKey, String impSecret) {
            return new PortOneTokenDto.Request(impKey, impSecret);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private int code;
        private String message;
        private ApiResponse response;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ApiResponse {
            private String access_token;
            private Long now;
            private Long expired_at;
        }
    }
}
