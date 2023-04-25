package com.liberty52.product.global.exception.external.forbidden;

import com.liberty52.product.global.exception.external.forbidden.NotYourResourceException;

public class NotYourOrderException extends NotYourResourceException {
    public NotYourOrderException(String id) {
        super("Order", id);
    }
}
