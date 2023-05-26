package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.TranslationService;
import com.liberty52.product.service.controller.dto.TranslationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TranslationController {
    private final TranslationService translationService;

    @PostMapping("/texts/translations")
    @ResponseStatus(HttpStatus.CREATED)
    public TranslationDto.Response translateText(@RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
                                                 @Validated @RequestBody TranslationDto.Request dto) {
        return translationService.translateText(authId, dto);
    }
}
