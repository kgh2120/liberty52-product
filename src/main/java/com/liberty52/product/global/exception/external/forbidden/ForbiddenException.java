package com.liberty52.product.global.exception.external.forbidden;

import com.liberty52.product.global.exception.external.AbstractApiException;
import com.liberty52.product.global.exception.external.ProductErrorCode;

public class ForbiddenException extends AbstractApiException {
    public ForbiddenException(String msg) {
        super(ProductErrorCode.FORBIDDEN, msg);
    }
}
