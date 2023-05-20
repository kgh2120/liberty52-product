package com.liberty52.product.service.controller.dto;

import com.liberty52.product.global.util.Validator;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
