package com.liberty52.product.global.adapter;

import com.liberty52.product.global.constants.TranslationConstants;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;

import static com.liberty52.product.global.exception.internal.PapagoApiException.*;

@Component
@Slf4j
public class PapagoApiClient {
    private WebClient webClient;
    private static final String BASE_URL = "https://openapi.naver.com/v1";
    private static final String PATH_TRANSLATION = "/papago/n2mt";
    private static final String PATH_LANG_DETECTION = "/papago/detectLangs";
    private static final String CONTENT_TYPE_KEY = HttpHeaders.CONTENT_TYPE;
    private static final String CONTENT_TYPE_VALUE = MediaType.APPLICATION_FORM_URLENCODED_VALUE + "; charset=UTF-8";
    private static final String HEADER_CLIENT_ID_KEY = "X-Naver-Client-Id";
    private static final String HEADER_CLIENT_SECRET_KEY = "X-Naver-Client-Secret";
    @Value("${papago.client-secret}")
    private String HEADER_CLIENT_SECRET_VALUE;
    @Value("${papago.client-id}")
    private String HEADER_CLIENT_ID_VALUE;

    @PostConstruct
    private void init() {
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE)
                .defaultHeader(HEADER_CLIENT_ID_KEY, HEADER_CLIENT_ID_VALUE)
                .defaultHeader(HEADER_CLIENT_SECRET_KEY, HEADER_CLIENT_SECRET_VALUE)
                .build();
    }

    public TranslationDto.Response postTranslation(TranslationConstants.LangCode source, TranslationConstants.LangCode target, String text) throws TooLongTextForTranslationException, TranslationSameSourceTargetException {
        return webClient.post()
                .uri(PATH_TRANSLATION)
                .body(BodyInserters.fromValue(new TranslationDto.Request(source, target, text)))
                .retrieve()
                .bodyToMono(TranslationDto.Response.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> {
                            System.out.println(ex.getResponseBodyAsString());
                            log.error("Error on {}\nexception: {}", PapagoApiClient.this.getClass().getName(),  ex.getResponseBodyAsString());
                            return ex.getStatusCode() == HttpStatus.BAD_REQUEST ? Mono.empty() : Mono.error(ex);
                        })
                .block();
    }

    public LangDetectionDto.Response postLangDetection(String text) {
        return webClient.post()
                .uri(PATH_LANG_DETECTION)
                .body(BodyInserters.fromValue(new LangDetectionDto.Request(text)))
                .retrieve()
                .bodyToMono(LangDetectionDto.Response.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> {
                            System.out.println(ex.getResponseBodyAsString());
                            log.error("Error on {}\nexception: {}", PapagoApiClient.this.getClass().getName(),  ex.getResponseBodyAsString());
                            return ex.getStatusCode() == HttpStatus.BAD_REQUEST ? Mono.empty() : Mono.error(ex);
                        })
                .block();
    }

    public static class LangDetectionDto {
        public static final class Request extends MultiValueMapAdapter<String, String> {
            public Request(String query) {
                super(new HashMap<>());
                this.add("query", query);
            }
        }
        public record Response(TranslationConstants.LangCode langCode) {
        }
    }

    public static class TranslationDto {
        public static final class Request extends MultiValueMapAdapter<String, String> {
            public Request(TranslationConstants.LangCode source, TranslationConstants.LangCode target, String text) throws TranslationSameSourceTargetException, TooLongTextForTranslationException {
                super(new HashMap<>());
                if(target==source) throw new TranslationSameSourceTargetException();
                if(text.length() > 1000) throw new TooLongTextForTranslationException();
                this.add("source", source.getCode());
                this.add("target", target.getCode());
                this.add("text", text);
            }
        }

        public record Response (
            Message message
        ) {
            public record Message (
                    Result result
            ) {
                public record Result (
                        TranslationConstants.LangCode srcLangType,
                        TranslationConstants.LangCode tarLangType,
                        String translatedText
                ) {}
            }
        }
    }
}
