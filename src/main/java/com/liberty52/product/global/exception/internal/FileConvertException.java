package com.liberty52.product.global.exception.internal;

public class FileConvertException extends S3UploaderException {
    public FileConvertException() {
        super("파일 변환 작업에서 오류가 발생하였습니다.");
    }
}
