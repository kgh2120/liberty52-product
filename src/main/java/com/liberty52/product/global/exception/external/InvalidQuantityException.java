package com.liberty52.product.global.exception.external;

import static com.liberty52.product.global.exception.external.ProductErrorCode.INVALID_QUANTITY;

public class InvalidQuantityException extends AbstractApiException{

    public InvalidQuantityException() {
        super(INVALID_QUANTITY);
    }
}
