package com.liberty52.product.global.exception.external;

public class ForbiddenException extends AbstractApiException {
    public ForbiddenException(String resourceName, String param) {
        super(ProductErrorCode.FORBIDDEN, String.format("The %s is not for the user. The header was %s", resourceName, param));
    }
}