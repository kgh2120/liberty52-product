package com.liberty52.product.global.exception.external.forbidden;

public class NotYourCustomProductException extends NotYourResourceException {
    public NotYourCustomProductException(String id) {
        super("CUSTOM_PRODUCT", id);
    }
}
