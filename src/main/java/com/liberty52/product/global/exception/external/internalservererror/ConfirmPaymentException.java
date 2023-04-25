package com.liberty52.product.global.exception.external.internalservererror;

public class ConfirmPaymentException extends InternalServerErrorException {
    public ConfirmPaymentException() {
        super("결제 시스템에 오류가 발생하여 응답하기 어렵습니다. 관리자에게 문의해주세요.");
    }
}
