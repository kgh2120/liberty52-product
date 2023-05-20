package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.adapter.DallEApiClient;
import com.liberty52.product.service.applicationservice.ImageGenerationService;
import com.liberty52.product.service.controller.dto.ImageGenerationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageGenerationServiceImpl implements ImageGenerationService {
    private final DallEApiClient dallEApiClient;
    @Override
    public ImageGenerationDto.Response generate(String authId, ImageGenerationDto.Request dto) {
        List<String> urls = dallEApiClient
                .generateImage(dto.prompt(),
                        dto.n(),
                        DallEApiClient.Dto.Request.Size.S1024,
                        DallEApiClient.Dto.Request.Format.URL,
                        authId)
                .data()
                .stream()
                .map(DallEApiClient.Dto.Response.GenerationData::url)
                .toList();

        return new ImageGenerationDto.Response(urls);
    }
}
