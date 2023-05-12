package com.liberty52.product.global.adapter.cloud.error;

import com.liberty52.product.global.exception.external.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FeignErrorCode implements ErrorCode {

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "FEIGN_ERROR - 다시 로그인 후 시도해주세요."),
    ERROR_4XX(HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR_5XX(HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR_UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private static final String DEFAULT_MESSAGE = "시스템에 오류가 발생하였습니다. 관리자에게 문의해주세요.";
    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final String errorCode = "PF-" + "0".repeat(Math.max(4-String.valueOf(this.ordinal() + 1).length(), 0)) + (this.ordinal() + 1);

    FeignErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    FeignErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.errorMessage = DEFAULT_MESSAGE;
    }

    public String getErrorName() {
        return this.name();
    }


}
