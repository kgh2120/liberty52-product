package com.liberty52.product.service.entity.payment;

import com.liberty52.product.global.exception.external.badrequest.BadRequestException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum BankType {
    KB("국민은행"), SC("SC제일은행"), GYEONG_NAM("경남은행"), GWANG_JU("광주은행"),
    IBK("기업은행"), NH("농협은행"), DAEGU("대구은행"), BUSAN("부산은행"), KDB("산업은행"),
    MG("새마을금고"), SH("수협은행"), SHIN_HAN("신한은행"), CU("신협은행"), KEB("외환은행"),
    WOORI("우리은행"), POST("우체국은행"), JEON_BUK("전북은행"), KAKAO("카카오뱅크"), K("케이뱅크"),
    HANA("하나은행"), CITY("한국씨티은행"), TOSS("토스뱅크")
    ;
    private final String koName;

    BankType(String koName) {
        this.koName = koName;
    }

    public static BankType getBankType(String target) {
        return Arrays.stream(BankType.values())
                .filter(bank -> Objects.equals(bank.koName, target))
                .findAny()
                .orElseThrow(() -> new BadRequestException("유효하지 않은 은행 종류입니다. 요청은행:"+target));
    }

}
