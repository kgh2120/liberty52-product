package com.liberty52.product.global.exception.external;

public class RequestForgeryPayException extends AbstractApiException {
    public RequestForgeryPayException() {
        super(ProductErrorCode.PAY_FORGERY_REQUEST);
    }
}
