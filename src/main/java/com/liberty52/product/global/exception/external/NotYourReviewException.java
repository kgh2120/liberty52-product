package com.liberty52.product.global.exception.external;

import static com.liberty52.product.global.exception.external.ProductErrorCode.NO_YOUR_REVIEW;

public class NotYourReviewException extends AbstractApiException{

    public NotYourReviewException() {
        super(NO_YOUR_REVIEW);
    }
}
