package com.liberty52.product;

import com.liberty52.product.global.adapter.s3.S3Uploader;
import com.liberty52.product.global.adapter.s3.S3UploaderApi;
import com.liberty52.product.global.exception.internal.S3UploaderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MockS3Test {
    @MockBean
    protected S3UploaderApi s3UploaderApi;

    @MockBean
    protected S3Uploader s3Uploader;

    @BeforeEach
    public void initMockBeans() {
        when(s3UploaderApi.upload(any())).thenReturn("mock s3 return");
        doNothing().when(s3UploaderApi).delete(any());
        try {
            when(s3Uploader.upload(any())).thenReturn("mock s3 return");
            doNothing().when(s3Uploader).delete(any());
        } catch (S3UploaderException e) {
            throw new RuntimeException(e);
        }
    }
}
