package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.TranslationDto;

public interface TranslationService {
    TranslationDto.Response translateText(String authId, TranslationDto.Request dto);
}
