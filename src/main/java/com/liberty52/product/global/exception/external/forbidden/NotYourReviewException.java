package com.liberty52.product.global.exception.external.forbidden;

import com.liberty52.product.global.exception.external.forbidden.NotYourResourceException;

public class NotYourReviewException extends NotYourResourceException {

    public NotYourReviewException(String id) {
        super("Review", id);
    }
}
