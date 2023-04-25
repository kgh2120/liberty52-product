package com.liberty52.product.global.exception.external.forbidden;

import com.liberty52.product.global.exception.external.forbidden.NotYourResourceException;

public class NotYourCartItemException extends NotYourResourceException {
    public NotYourCartItemException(String id) {
        super("CartItem(CustomProduct)", id);
    }
}
