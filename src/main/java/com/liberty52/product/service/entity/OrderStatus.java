package com.liberty52.product.service.entity;

public enum OrderStatus {

    // 주문 됨, 제작 중, 배송 중, 완료 , 입금 대기
    CANCEL_REQUESTED, CANCELED, REFUND, READY, ORDERED, MAKING, DELIVERING, COMPLETE, WAITING_DEPOSIT

}
