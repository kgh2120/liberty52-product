package com.liberty52.product.global.exception.external;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProductErrorCode implements ErrorCode{
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND),
    FORBIDDEN(HttpStatus.FORBIDDEN),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND),
    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND),
    OPTION_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus httpStatus;
    private final String errorMessage;

    private final String errorCode = "A-" + "0".repeat(Math.max(4-String.valueOf(this.ordinal() + 1).length(), 0)) + (this.ordinal() + 1);

    ProductErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    ProductErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.errorMessage = "";
    }

    public String getErrorName() {
        return this.name();
    }
}