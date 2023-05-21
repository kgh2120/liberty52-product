package com.liberty52.product.global.adapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.liberty52.product.global.exception.internal.DallEApiClientExceptions;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class DallEApiClient {
    @Value("${openai.api-key}")
    private String apiKey;
    private WebClient webClient;
    private static final String BASE_URL = "https://api.openai.com/v1";
    private static final String PATH_GENERATION = "/images/generations";

    @PostConstruct
    private void init() {
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .exchangeStrategies( // b64 로 받을 때 메모리 제한에 걸리는 것에 대한 해결. limit 없애버림.
                        ExchangeStrategies.builder()
                                .codecs(config -> config.defaultCodecs().maxInMemorySize(-1))
                                .build())
                .build();
    }

    public Dto.Response.URL generateImageAsUrl(String prompt, int n, Dto.Request.Size size, String user) {
        return generateImage(prompt, n, size, user, Dto.Request.Format.URL, Dto.Response.URL.class);
    }

    public Dto.Response.B64Json generateImageAsB64Json(String prompt, int n, Dto.Request.Size size, String user) {
        return generateImage(prompt, n, size, user, Dto.Request.Format.B64_JSON, Dto.Response.B64Json.class);
    }

    private <T extends Dto.Response<?>> T generateImage(String prompt, int n, Dto.Request.Size size, String user, Dto.Request.Format format, Class<T> responseType) {
        Dto.Request request = new Dto.Request(prompt, n, size, format, user);
        return webClient.post()
                .uri(PATH_GENERATION)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(responseType)
                .onErrorResume(WebClientResponseException.class,
                        ex -> {
                            System.out.println(ex.getResponseBodyAsString());
                            log.error("Error on {}\nexception: {}", DallEApiClient.this.getClass().getName(),  ex.getResponseBodyAsString());
                            return ex.getStatusCode() == HttpStatus.BAD_REQUEST ? Mono.empty() : Mono.error(ex);
                        })
                .block();
    }

    public static class Dto {
        @Getter
        public static final class Request {
            private final String prompt;
            private final int n;
            private final String size;
            @JsonProperty("response_format")
            private final String responseFormat;
            private final String user;

            public Request(String prompt, int n, Size size, Format responseFormat, String user) {
                this.prompt = prompt;
                this.n = n;
                this.size = size.get();
                this.responseFormat = responseFormat.getValue();
                this.user = user;
                validValues();
            }

            private void validValues() {
                if(prompt.length() < 1 || prompt.length() > 1000)
                    throw new DallEApiClientExceptions.InvalidPrompt();
                if(n < 1 || n > 10)
                    throw new DallEApiClientExceptions.InvalidN();
                if(!Format.contains(responseFormat))
                    throw new DallEApiClientExceptions.InvalidResponseFormat();
                if(user != null && user.isBlank())
                    throw new DallEApiClientExceptions.InvalidUser();
            }

            @Getter
            public enum Format {
                URL("url"),
                B64_JSON("b64_json"),
                ;

                final String value;
                Format(String value) {
                    this.value = value;
                }

                public static boolean contains(String responseFormat) {
                    for (Format e : values()) {
                        if(e.value.equals(responseFormat)) return true;
                    }
                    return false;
                }
            }

            public enum Size {
                S256(256),
                S512(512),
                S1024(1024);

                final int width;
                final int height;

                Size(int width) {
                    this.width = this.height = width;
                }

                public String get() {
                    return width + "x" + height;
                }
            }
        }

        @Getter
        public abstract static class Response<T extends Response.Data> {
            private Long created;
            private List<T> data;

            public static class URL extends Response<Data.URL> {
            }

            public static class B64Json extends Response<Data.B64Json> {
            }

            public abstract static class Data {
                @Getter
                public static class URL extends Data {
                    private String url;
                }
                @Getter
                public static class B64Json extends Data {
                    @JsonProperty("b64_json")
                    private String b64Json;
                }
            }
        }
    }

}
