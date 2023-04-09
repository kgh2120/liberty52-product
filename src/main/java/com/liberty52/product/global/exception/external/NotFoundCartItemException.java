package com.liberty52.product.global.exception.external;

public class NotFoundCartItemException extends AbstractApiException {
    public NotFoundCartItemException() {
        super(ProductErrorCode.NOT_FOUND_CART_ITEM);
    }
}
