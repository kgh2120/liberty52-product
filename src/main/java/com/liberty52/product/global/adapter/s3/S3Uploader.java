package com.liberty52.product.global.adapter.s3;

import com.liberty52.product.global.exception.internal.S3UploaderException;
import org.springframework.web.multipart.MultipartFile;

public interface S3Uploader extends S3Uploader_ {
    String upload(MultipartFile multipartFile) throws S3UploaderException;

    String uploadOfByte(byte[] b);
}
