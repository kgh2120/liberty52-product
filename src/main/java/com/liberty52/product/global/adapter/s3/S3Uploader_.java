package com.liberty52.product.global.adapter.s3;

import com.liberty52.product.global.exception.internal.S3UploaderException;
import org.springframework.web.multipart.MultipartFile;

/**
 * package only interface
 */
interface S3Uploader_ {
    String upload(MultipartFile multipartFile) throws S3UploaderException;
    void delete(String url);
}
