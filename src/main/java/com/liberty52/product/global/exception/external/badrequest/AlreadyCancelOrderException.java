package com.liberty52.product.global.exception.external.badrequest;

public class AlreadyCancelOrderException extends BadRequestException {
    public AlreadyCancelOrderException() {
        super("해당 주문은 이미 취소되었거나 취소 요청된 주문입니다.");
    }
}
