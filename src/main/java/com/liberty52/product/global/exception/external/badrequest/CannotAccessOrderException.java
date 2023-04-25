package com.liberty52.product.global.exception.external.badrequest;

import com.liberty52.product.global.exception.external.badrequest.BadRequestException;

public class CannotAccessOrderException extends BadRequestException {

    public CannotAccessOrderException() {
        super("해당 주문에 접근할 수 없습니다.");
    }
}
