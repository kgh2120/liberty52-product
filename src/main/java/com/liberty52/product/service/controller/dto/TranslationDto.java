package com.liberty52.product.service.controller.dto;

import com.liberty52.product.global.constants.TranslationConstants;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class TranslationDto {
    @Data
    public static class Request {
        @Size(min = 10, max = 5000)
        private String text;

        public static Request of(String text) {
            Request request = new Request();
            request.text = text;
            return request;
        }
    }
    @Data
    public static class Response {
        private TranslationConstants.LangCode source;
        private TranslationConstants.LangCode target;
        private String translatedText;

        public static Response of(TranslationConstants.LangCode source, TranslationConstants.LangCode target, String translatedText) {
            Response response = new Response();
            response.source = source;
            response.target = target;
            response.translatedText = translatedText;
            return response;
        }
    }
}
