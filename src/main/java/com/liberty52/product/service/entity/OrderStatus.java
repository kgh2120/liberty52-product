package com.liberty52.product.service.entity;

import com.liberty52.product.global.exception.external.badrequest.BadRequestException;
import lombok.Getter;

public enum  OrderStatus {
    CANCEL_REQUESTED("취소요청"), CANCELED("주문취소"), REFUND("환불완료"),
    READY("주문접수"), WAITING_DEPOSIT("입금대기"), ORDERED("주문완료"), MAKING("제작시작"), DELIVERING("배송시작"), COMPLETE("배송완료");

    @Getter
    private final String name;
    OrderStatus(String name){
        this.name = name;
    }

    public static OrderStatus getPreviousStatus(OrderStatus orderStatus) {
        if (orderStatus == OrderStatus.READY) {
            throw new BadRequestException("비정상적인 상태변경입니다.");
        }
        return OrderStatus.values()[orderStatus.ordinal() - 1];
    }
}
