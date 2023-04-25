package com.liberty52.product.global.exception.external.notfound;

import com.liberty52.product.global.exception.external.AbstractApiException;
import com.liberty52.product.global.exception.external.ProductErrorCode;

public class NotFoundException extends AbstractApiException {
    public NotFoundException(String msg) {
        super(ProductErrorCode.RESOURCE_NOT_FOUND, msg);
    }
}
