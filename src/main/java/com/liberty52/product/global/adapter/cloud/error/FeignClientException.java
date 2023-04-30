package com.liberty52.product.global.adapter.cloud.error;

import com.liberty52.product.global.exception.external.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FeignClientException extends RuntimeException implements ErrorCode {

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorName;
    private final String errorMessage;

    public FeignClientException(FeignErrorCode code) {
        this.httpStatus = code.getHttpStatus();
        this.errorCode = code.getErrorCode();
        this.errorName = code.getErrorName();
        this.errorMessage = code.getErrorMessage();
    }

    public FeignClientException() {
        this(FeignErrorCode.ERROR_UNKNOWN);
    }
}
