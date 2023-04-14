package com.liberty52.product.global.exception.external;

public class NotFoundCartProductException extends AbstractApiException {
    public NotFoundCartProductException() {
        super(ProductErrorCode.NOT_FOUND_CART_Product);
    }
}
