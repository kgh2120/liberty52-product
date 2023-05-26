package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.adapter.PapagoApiClient;
import com.liberty52.product.global.constants.TranslationConstants;
import com.liberty52.product.global.exception.internal.PapagoApiException;
import com.liberty52.product.service.controller.dto.TranslationDto;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TranslationServiceImplTest {
    @Autowired
    TranslationServiceImpl service;
    @MockBean
    PapagoApiClient apiClient;

    String authId = UUID.randomUUID().toString();

    @Test
    void basicPath() throws PapagoApiException.TooLongTextForTranslationException, PapagoApiException.TranslationSameSourceTargetException {
        String originalText = UUID.randomUUID().toString();
        String translatedText = UUID.randomUUID().toString();
        BDDMockito.given(apiClient.postLangDetection(originalText))
                .willReturn(new PapagoApiClient.LangDetectionDto.Response(TranslationConstants.LangCode.KOREAN));
        BDDMockito.given(apiClient.postTranslation(TranslationConstants.LangCode.KOREAN, TranslationConstants.LangCode.ENGLISH, originalText))
                .willReturn(new PapagoApiClient.TranslationDto.Response(new PapagoApiClient.TranslationDto.Response.Message(new PapagoApiClient.TranslationDto.Response.Message.Result(TranslationConstants.LangCode.KOREAN, TranslationConstants.LangCode.ENGLISH, translatedText))));

        TranslationDto.Response response = service.translateText(authId, TranslationDto.Request.of(originalText));
        assertEquals(TranslationConstants.LangCode.KOREAN, response.getSource());
        assertEquals(TranslationConstants.LangCode.ENGLISH, response.getTarget());
        assertEquals(translatedText, response.getTranslatedText());
    }
}
