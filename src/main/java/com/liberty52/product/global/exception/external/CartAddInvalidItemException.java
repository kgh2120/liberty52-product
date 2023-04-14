package com.liberty52.product.global.exception.external;

import static com.liberty52.product.global.exception.external.ProductErrorCode.CART_ADD_INVALID_ITEM;

public class CartAddInvalidItemException extends AbstractApiException{

    public CartAddInvalidItemException() {
        super(CART_ADD_INVALID_ITEM);
    }
}
