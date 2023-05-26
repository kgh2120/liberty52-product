package com.liberty52.product.global.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.core.convert.converter.Converter;

public class TranslationConstants {
    public enum LangCode {
        KOREAN("ko"), ENGLISH("en"), JAPANESE("ja"), CHINESE_SIMPLIFIED("zh-CN"), CHINESE_TRADITIONAL("zh-TW"),
        VIETNAMESE("vi"), INDONESIAN("id"), THAI("th"), GERMAN("de"), RUSSIAN("ru"), SPANISH("es"),
        ITALY("it"), FRENCH("fr");

        @JsonValue
        private final String code;
        public static LangCode DEFAULT = ENGLISH;
        LangCode(String code) {
            this.code = code;
        }

        public static LangCode of(String code) {
            for (LangCode value : values()) {
                if(value.code.equals(code))
                    return value;
            }
            return ENGLISH;
        }

        public String getCode() {
            return code;
        }
    }

    public static class LangCodeConverter implements Converter<String, LangCode> {
        @Override
        public LangCode convert(String code) {
            return LangCode.of(code);
        }
    }

}
