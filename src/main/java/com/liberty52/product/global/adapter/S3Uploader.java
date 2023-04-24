package com.liberty52.product.global.adapter;

import org.springframework.web.multipart.MultipartFile;

public interface S3Uploader {
    String upload(MultipartFile multipartFile);
    void delete(String imageUrl);
}
