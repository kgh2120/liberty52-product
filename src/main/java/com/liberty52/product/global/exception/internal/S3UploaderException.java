package com.liberty52.product.global.exception.internal;

public class S3UploaderException extends Exception {
    public S3UploaderException() {
        super("파일 업로드 작업에 오류가 발생하였습니다.");
    }

    public S3UploaderException(String msg) {
        super(msg);
    }
}
