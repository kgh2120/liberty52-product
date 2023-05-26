package com.liberty52.product.global.exception.external.internalservererror;

import com.liberty52.product.global.exception.external.AbstractApiException;
import com.liberty52.product.global.exception.external.ProductErrorCode;
import com.liberty52.product.global.exception.internal.PapagoApiException;

public class InternalServerErrorException extends AbstractApiException {
    public InternalServerErrorException(String msg) {
        super(ProductErrorCode.INTERNAL_SERVER_ERROR, msg);
    }

}
