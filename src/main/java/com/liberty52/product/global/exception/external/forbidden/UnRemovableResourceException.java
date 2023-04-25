package com.liberty52.product.global.exception.external.forbidden;

import com.liberty52.product.global.exception.external.forbidden.ForbiddenException;

public class UnRemovableResourceException extends ForbiddenException {
    public UnRemovableResourceException(String resourceName, String resourceId) {
        super(String.format("%s cannot be removed. The id of %s was %s", resourceName, resourceName, resourceId));
    }
}
