package com.liberty52.product.global.exception.external;

public class BadRequestException extends AbstractApiException {
    public BadRequestException(String msg) {
        super(ProductErrorCode.BAD_REQUEST, msg);
    }
}
