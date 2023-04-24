package com.liberty52.product.global.exception.external;

public class InternalServerException extends AbstractApiException {
    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
