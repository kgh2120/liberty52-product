package com.liberty52.product.global.exception.external.badrequest;

public class CartAddInvalidItemException extends BadRequestException {

    public CartAddInvalidItemException() {
        super("해당 상품은 장바구니에 담길 수 없습니다. (사유 : 이미 주문된 제품)");
    }
}
