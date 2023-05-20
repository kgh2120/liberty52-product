package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class OrderRefundDto {

    @Getter
    @Builder
    public static class Request {
        @NotBlank
        private String orderId;
        @NotNull
        private Integer fee;
    }

}
