package com.liberty52.product.global.adapter.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.liberty52.product.global.exception.internal.FileConvertException;
import com.liberty52.product.global.exception.internal.FileNullException;
import com.liberty52.product.global.exception.internal.FileTypeIsNotImageException;
import com.liberty52.product.global.exception.internal.S3UploaderException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.ion.NullValueException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class S3UploaderImpl implements S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${my-storage.dir.product.custom}") // product/custom
    private String customProductPath;

    @Override
    public String upload(MultipartFile multipartFile) throws S3UploaderException {
        try {
            Objects.requireNonNull(multipartFile);
            return uploadFile(multipartFile);
        } catch (NullPointerException e) {
            throw new FileNullException();
        } catch (IOException e) {
            throw new S3UploaderException();
        }
    }

    @Override
    public void delete(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }
        deleteS3(imageUrl);
    }

    private String uploadFile(MultipartFile multipartFile) throws IOException, FileTypeIsNotImageException, FileConvertException {
        String fileName = this.makeFileName(multipartFile);

        File file = this.convert(multipartFile);

        return upload(file, fileName);
    }

    private String upload(File uploadFile, String fileName) {
        fileName = customProductPath + "/" + fileName;

        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드

        removeLocalTempFile(uploadFile);

        return uploadImageUrl;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeLocalTempFile(File file) {
        boolean isDeleted = file.delete();
        if ( !isDeleted ) {
            log.error("S3Uploader ERROR: Can not Remove Local File");
        }
    }

    // 로컬에 파일 업로드 하기
    private File convert(MultipartFile multipartFile) throws IOException, FileConvertException {
        File file = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());
        if (file.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(file)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(multipartFile.getBytes());
            }
            return file;
        }
        throw new FileConvertException();
    }

    private String makeFileName(MultipartFile multipartFile) throws FileTypeIsNotImageException {
        String extension = this.getExtension(multipartFile);

        return UUID.randomUUID() + extension;
    }

    private String getExtension(MultipartFile multipartFile) throws NullValueException, FileTypeIsNotImageException {
        int extensionIndex = Objects.requireNonNull(multipartFile.getOriginalFilename()).lastIndexOf(".");

        if (!Objects.requireNonNull(multipartFile.getContentType()).contains("image") || extensionIndex < 0) {
            throw new FileTypeIsNotImageException();
        }

        return multipartFile.getOriginalFilename().substring(extensionIndex);
    }

    private void deleteS3(String key) {
        if (amazonS3Client.doesObjectExist(bucket, key)) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
        }
    }

}