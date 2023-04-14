package com.liberty52.product.global.exception.external;

public class S3UploaderException extends AbstractApiException {
    public S3UploaderException() {
        super(ProductErrorCode.FILE_UPLOAD_ERROR);
    }
}
