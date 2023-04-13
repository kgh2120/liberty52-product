package com.liberty52.product.global.exception.external;

public class UnRemovableResourceException extends AbstractApiException {
    public UnRemovableResourceException(String resourceName, String resourceId) {
        super(ProductErrorCode.FORBIDDEN, String.format("%s cannot be removed. The id of %s was %s", resourceName, resourceName, resourceId));
    }
}
