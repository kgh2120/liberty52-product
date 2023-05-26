package com.liberty52.product.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liberty52.product.global.config.DBInitConfig;
import com.liberty52.product.global.constants.TranslationConstants;
import com.liberty52.product.service.applicationservice.TranslationService;
import com.liberty52.product.service.controller.dto.TranslationDto;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TranslationController.class)
class TranslationControllerTest {
    @InjectMocks
    TranslationController controller;
    @MockBean
    TranslationService service;
    @Autowired
    MockMvc mockMvc;
    String authId = DBInitConfig.DBInitService.AUTH_ID;

    @Test
    void translateText() throws Exception {
        String text = "ㅇ".repeat(40);
        String translatedText = "some eng";
        TranslationDto.Request request = TranslationDto.Request.of(text);
        BDDMockito.given(service.translateText(authId, request))
                .willReturn(TranslationDto.Response.of(TranslationConstants.LangCode.KOREAN, TranslationConstants.LangCode.ENGLISH, translatedText));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/texts/translations")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, authId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.translatedText").value(translatedText))
                .andExpect(jsonPath("$.source").value(TranslationConstants.LangCode.KOREAN.getCode()))
                .andExpect(jsonPath("$.target").value(TranslationConstants.LangCode.ENGLISH.getCode()));
    }

    @Test
    void validationError() throws Exception {
        String translatedText = "some eng";
        BDDMockito.given(service.translateText(BDDMockito.any(), BDDMockito.any()))
                .willReturn(TranslationDto.Response.of(TranslationConstants.LangCode.KOREAN, TranslationConstants.LangCode.ENGLISH, translatedText));

        String invalidMinText = "1234";
        TranslationDto.Request invalidMinRequest = TranslationDto.Request.of(invalidMinText);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/texts/translations")
                        .content(new ObjectMapper().writeValueAsString(invalidMinRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, authId))
                .andExpect(status().isBadRequest());

        String invalidMaxText = "?".repeat(5001);
        TranslationDto.Request invalidMaxRequest = TranslationDto.Request.of(invalidMaxText);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/texts/translations")
                        .content(new ObjectMapper().writeValueAsString(invalidMaxRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, authId))
                .andExpect(status().isBadRequest());
    }
}
