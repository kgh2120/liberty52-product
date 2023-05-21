package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.adapter.DallEApiClient;
import com.liberty52.product.global.adapter.s3.S3Uploader;
import com.liberty52.product.service.applicationservice.ImageGenerationService;
import com.liberty52.product.service.controller.dto.ImageGenerationDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageGenerationServiceImpl implements ImageGenerationService {
    private final DallEApiClient dallEApiClient;
    private final S3Uploader s3Uploader;

    @Override
    public ImageGenerationDto.Response generate(String authId, ImageGenerationDto.Request dto) {
        List<String> urls = dallEApiClient
                .generateImageAsB64Json(dto.prompt(),
                        dto.n(),
                        DallEApiClient.Dto.Request.Size.S1024,
                        authId)
                .getData()
                .stream()
                .map(DallEApiClient.Dto.Response.Data.B64Json::getB64Json)
                .map(Base64::decodeBase64)
                .map(s3Uploader::uploadOfByte)
                .toList();
        return new ImageGenerationDto.Response(urls);
    }
}
