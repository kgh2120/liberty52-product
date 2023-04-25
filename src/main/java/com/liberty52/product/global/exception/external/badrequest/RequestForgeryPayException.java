package com.liberty52.product.global.exception.external.badrequest;

import com.liberty52.product.global.exception.external.badrequest.BadRequestException;

public class RequestForgeryPayException extends BadRequestException {
    public RequestForgeryPayException() {
        super("결제가 위조된 가능성이 있습니다. 환불처리 후 다시 시도해주세요.");
    }
}
