package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

public class DeliveryOptionFeeModify {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull
        @Min(0)
        @Max(1_000_000)
        private int fee;
    }

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
