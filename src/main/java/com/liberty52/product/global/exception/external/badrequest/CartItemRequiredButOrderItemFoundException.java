package com.liberty52.product.global.exception.external.badrequest;

public class CartItemRequiredButOrderItemFoundException extends BadRequestException {
    public CartItemRequiredButOrderItemFoundException(String customProductId) {
        super(String.format("CartItem의 id가 필요합니다. 요청으로 받은 id(%s)는 OrderItem의 id입니다.", customProductId));
    }
}
