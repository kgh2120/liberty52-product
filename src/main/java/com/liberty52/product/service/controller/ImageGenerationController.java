package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.ImageGenerationService;
import com.liberty52.product.service.controller.dto.ImageGenerationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ImageGenerationController {
    private final ImageGenerationService service;

    @PostMapping("/images/generations")
    @ResponseStatus(HttpStatus.CREATED)
    public ImageGenerationDto.Response generateImage(@RequestHeader(HttpHeaders.AUTHORIZATION) String authId, // 어뷰징 방지.
                                            @Validated @RequestBody ImageGenerationDto.Request dto) {
        return service.generateImage(authId, dto);
    }
}
