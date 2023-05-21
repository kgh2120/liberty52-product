package com.liberty52.product.service.controller.dto;

import com.liberty52.product.global.util.Validator;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class OrderCancelDto {

    @Getter
    @Builder
    public static class Request {
        @NotBlank
        private String orderId;
        @NotBlank
        private String reason;
        private String refundBank;
        private String refundHolder;
        private String refundAccount;
        private String refundPhoneNum;

        public RefundVO getRefundVO() {
            if (Validator.areNullOrBlank(refundBank, refundHolder, refundAccount, refundPhoneNum)) {
                throw new IllegalArgumentException("refund value cannot be null or blank");
            }
            return new RefundVO(refundBank, refundHolder, refundAccount, refundPhoneNum);
        }

        @Getter
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static class RefundVO {
            private String refundBank;
            private String refundHolder;
            private String refundAccount;
            private String refundPhoneNum;

            public static RefundVO forTest() {
                return new RefundVO("하나은행", "환불자", "1234-1234-213", "01012341234");
            }
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {
        private String message;
        public static Response of(String message) {
            return new Response(message);
        }
    }

}
