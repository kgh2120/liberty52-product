package com.liberty52.product.service.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

public class DeliveryOptionFeeRetrieve {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private static final DecimalFormat PRICE_FORMAT;
        static {
            PRICE_FORMAT = new DecimalFormat("###,###");
        }

        private String fee;
        private String feeUpdatedAt;

        public static Response fromDto(DeliveryOptionDto dto) {
            return new Response(PRICE_FORMAT.format(dto.getFee()), dto.getFeeUpdatedAt().toString());
        }
    }
}
