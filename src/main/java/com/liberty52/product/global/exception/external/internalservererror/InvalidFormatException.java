package com.liberty52.product.global.exception.external.internalservererror;

import com.liberty52.product.global.exception.external.AbstractApiException;

public class InvalidFormatException extends InternalServerErrorException {
    public InvalidFormatException() {
        super("데이터가 올바른 형태를 갖추지 못했습니다.");
    }

}
