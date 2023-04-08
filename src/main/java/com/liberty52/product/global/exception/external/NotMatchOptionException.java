package com.liberty52.product.global.exception.external;

public class NotMatchOptionException extends AbstractApiException {
    public NotMatchOptionException() {
        super(ProductErrorCode.NOT_MATCH_OPTION);
    }
}
