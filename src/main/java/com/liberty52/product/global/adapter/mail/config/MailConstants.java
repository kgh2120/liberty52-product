package com.liberty52.product.global.adapter.mail.config;

public class MailConstants {

    public static class Title {
        public static class Customer {
            public static final String REQUEST_DEPOSIT = "[Liberty52] 입금 요청 안내";
            public static final String CARD_ORDERED_COMPLETED = "[Liberty52] 카드결제 주문 확인 안내";
            public static final String VBANK_ORDERED_COMPLETED = "[Liberty52] 가상계좌 결제 주문 확인 안내";
            public static final String ORDER_CANCELED = "[Liberty52] 요청하신 주문 취소가 처리되었습니다.";
        }

        public static class Admin {
            public static String ORDER_CANCELED(String customerName) {
                return String.format(ORDER_CANCELED, customerName);
            }
            private static final String ORDER_CANCELED = "[리버티52 시스템] 주문 취소 완료 - 주문자: %s";

            public static String ORDER_CANCEL_REQUESTED(String customerName) {
                return String.format(ORDER_CANCEL_REQUESTED, customerName);
            }
            private static final String ORDER_CANCEL_REQUESTED = "[리버티52 시스템] 주문 취소 요청 - 주문자: %s";
        }
    }

}
