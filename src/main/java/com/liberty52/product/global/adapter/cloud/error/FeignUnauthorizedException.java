package com.liberty52.product.global.adapter.cloud.error;

public class FeignUnauthorizedException extends FeignClientException {
    public FeignUnauthorizedException() {
        super(FeignErrorCode.UNAUTHORIZED);
    }
}
