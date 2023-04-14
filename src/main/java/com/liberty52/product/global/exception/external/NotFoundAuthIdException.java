package com.liberty52.product.global.exception.external;

public class NotFoundAuthIdException extends AbstractApiException {
    public NotFoundAuthIdException() {
        super(ProductErrorCode.NOT_FOUND_AUTH_ID);
    }
}
