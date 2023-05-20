package com.liberty52.product.global.adapter.portone.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PortOneCancelDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {

        private String imp_uid;
        private String reason;
        private Double amount;
        private Double checksum;

        public static PortOneCancelDto.Request of(String imp_uid, String reason, Long amount, Long checksum) {
            Request request = new Request();
            request.imp_uid = imp_uid;
            request.reason = reason;
            request.amount = Double.valueOf(amount);
            request.checksum = Double.valueOf(checksum);
            return request;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Response {

        private int code;
        private String message;

    }
}
