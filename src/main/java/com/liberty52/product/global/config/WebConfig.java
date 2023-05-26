package com.liberty52.product.global.config;

import com.liberty52.product.global.constants.TranslationConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new TranslationConstants.LangCodeConverter());
    }
}
