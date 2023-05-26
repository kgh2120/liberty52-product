package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.adapter.PapagoApiClient;
import com.liberty52.product.global.constants.TranslationConstants;
import com.liberty52.product.global.exception.external.internalservererror.InternalServerErrorException;
import com.liberty52.product.global.exception.internal.PapagoApiException;
import com.liberty52.product.service.applicationservice.TranslationService;
import com.liberty52.product.service.controller.dto.TranslationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {
    private final PapagoApiClient papagoApiClient;

    @Override
    public TranslationDto.Response translateText(String authId, TranslationDto.Request dto) {
        try {
            TranslationConstants.LangCode source = papagoApiClient.postLangDetection(dto.getText()).langCode();
            TranslationConstants.LangCode target = TranslationConstants.LangCode.ENGLISH;
            PapagoApiClient.TranslationDto.Response.Message.Result result = papagoApiClient.postTranslation(source, target, dto.getText()).message().result();
            return TranslationDto.Response.of(result.srcLangType(), result.tarLangType(), result.translatedText());
        } catch (PapagoApiException.TooLongTextForTranslationException e) {
            // Logically Unreachable
            throw new InternalServerErrorException("Logically unreachable but reached by " + e.getClass().getSimpleName());
        } catch (PapagoApiException.TranslationSameSourceTargetException e) {
            return TranslationDto.Response.of(TranslationConstants.LangCode.ENGLISH, TranslationConstants.LangCode.ENGLISH, dto.getText());
        }
    }
}
