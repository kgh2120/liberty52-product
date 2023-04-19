package com.liberty52.product.global.exception.external;

public class NotYourResourceException extends AbstractApiException {
    public NotYourResourceException(String resourceName, String id) {
        super(ProductErrorCode.FORBIDDEN, String.format("The %s is not for the user. The header was %s", resourceName, id));
    }
}
