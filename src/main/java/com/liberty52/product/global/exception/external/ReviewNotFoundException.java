package com.liberty52.product.global.exception.external;

import static com.liberty52.product.global.exception.external.ProductErrorCode.REVIEW_NOT_FOUND;

public class ReviewNotFoundException extends AbstractApiException{

    public ReviewNotFoundException() {
        super(REVIEW_NOT_FOUND);
    }
}
