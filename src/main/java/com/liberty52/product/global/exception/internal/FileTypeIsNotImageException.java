package com.liberty52.product.global.exception.internal;

public class FileTypeIsNotImageException extends S3UploaderException {
    public FileTypeIsNotImageException() {
        super("이미지 형식의 파일만 업로드할 수 있습니다.");
    }
}
