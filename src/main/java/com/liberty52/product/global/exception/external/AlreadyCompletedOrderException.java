package com.liberty52.product.global.exception.external;

import static com.liberty52.product.global.exception.external.ProductErrorCode.ALREADY_COMPLETED_ORDER;

public class AlreadyCompletedOrderException extends AbstractApiException{

    public AlreadyCompletedOrderException() {
        super(ALREADY_COMPLETED_ORDER);
    }
}
