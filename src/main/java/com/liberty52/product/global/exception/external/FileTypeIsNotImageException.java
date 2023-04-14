package com.liberty52.product.global.exception.external;

public class FileTypeIsNotImageException extends AbstractApiException {
    public FileTypeIsNotImageException() {
        super(ProductErrorCode.FILE_TYPE_IS_NOT_IMAGE_ERROR);
    }
}
