package com.liberty52.product.global.adapter.s3;

import com.liberty52.product.global.exception.external.internalservererror.InternalServerErrorException;
import com.liberty52.product.global.exception.internal.S3UploaderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class S3UploaderApiImpl implements S3UploaderApi {
    private final S3Uploader s3Uploader;
    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            return s3Uploader.upload(multipartFile);
        } catch (S3UploaderException e) {
            throw new InternalServerErrorException("S3 에러로 인해 이미지 업로드에 실패하였습니다.");
        }
    }

    @Override
    public void delete(String imageUrl) {
        s3Uploader.delete(imageUrl);
    }
}
