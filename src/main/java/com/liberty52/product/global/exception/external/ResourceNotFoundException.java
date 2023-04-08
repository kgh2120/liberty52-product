package com.liberty52.product.global.exception.external;

public class ResourceNotFoundException extends AbstractApiException{
    public ResourceNotFoundException(String resourceName, String by, String param) {
        super(ProductErrorCode.RESOURCE_NOT_FOUND, String.format("%s is not found by %s. The %s was %s", resourceName, by, by, param));
    }
}
