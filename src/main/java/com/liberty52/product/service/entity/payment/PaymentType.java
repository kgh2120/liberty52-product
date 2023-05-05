package com.liberty52.product.service.entity.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PaymentType {
    CARD("신용카드"), VBANK("가상 계좌"), NAVER_PAY("네이버 페이")


    ;

    private final String korName;

}
