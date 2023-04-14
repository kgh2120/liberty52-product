package com.liberty52.product.global.exception.external;

public class FileConvertException extends AbstractApiException {
    public FileConvertException() {
        super(ProductErrorCode.FILE_CONVERT_ERROR);
    }
}
