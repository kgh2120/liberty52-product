package com.liberty52.product.global.exception.external.forbidden;

import com.liberty52.product.global.exception.external.forbidden.ForbiddenException;

public abstract class NotYourResourceException extends ForbiddenException {

    public NotYourResourceException(String resourceName, String id) {
        super(makeMessage(resourceName, id));
    }

    private static String makeMessage(String resourceName, String id) {
        return String.format("The %s is not for the user. The header was %s", resourceName, id);
    }
}
