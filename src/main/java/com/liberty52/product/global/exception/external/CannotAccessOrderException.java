package com.liberty52.product.global.exception.external;

import static com.liberty52.product.global.exception.external.ProductErrorCode.CANNOT_ACCESS_ORDER;

public class CannotAccessOrderException extends AbstractApiException{

    public CannotAccessOrderException() {
        super(CANNOT_ACCESS_ORDER);
    }
}
