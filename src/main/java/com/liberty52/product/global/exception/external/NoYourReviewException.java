package com.liberty52.product.global.exception.external;

import static com.liberty52.product.global.exception.external.ProductErrorCode.NO_YOUR_REVIEW;

public class NoYourReviewException extends AbstractApiException{

    public NoYourReviewException() {
        super(NO_YOUR_REVIEW);
    }
}
