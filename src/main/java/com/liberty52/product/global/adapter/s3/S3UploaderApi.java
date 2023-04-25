package com.liberty52.product.global.adapter.s3;

import org.springframework.web.multipart.MultipartFile;

public interface S3UploaderApi extends S3Uploader_{
    /**
     * 내부 에러 발생 시 InternalServerErrorException 발생 -> RestControllerAdvice 핸들링
     */
    String upload(MultipartFile multipartFile);
    void delete(String url);
}
