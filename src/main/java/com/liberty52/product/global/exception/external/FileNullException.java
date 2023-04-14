package com.liberty52.product.global.exception.external;

public class FileNullException extends AbstractApiException {
    public FileNullException() {
        super(ProductErrorCode.FILE_NULL_ERROR);
    }
}
