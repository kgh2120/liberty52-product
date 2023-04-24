package com.liberty52.product.global.adapter.portone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liberty52.product.global.adapter.portone.dto.*;
import com.liberty52.product.global.adapter.portone.exception.PortOne4xxResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class PortOneRequestClient {

    //TODO Config 서버로 암호화하여 Value 로 가져올 것.
    private final String PORT_ONE_API_KEY = "3300384714738658";
    //TODO Config 서버로 암호화하여 Value 로 가져올 것.
    private final String PORT_ONE_SECRET_KEY = "NxQ4Ou0zVJhR2S4EJrng7OhW4j2LJmZlD3RtMExszd5R1sEQdEdp8loRLAV5LYmhWo2Wc8NXaNQU7coX";
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PortOneToken getAccessToken() {
        try {
            PortOneTokenResponseDto responseDto = webClient.post()
                    .uri("/users/getToken")
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(
                            PortOneTokenRequestDto.of(PORT_ONE_API_KEY, PORT_ONE_SECRET_KEY)
                    ))
                    .retrieve()
                    .bodyToMono(PortOneTokenResponseDto.class)
                    .block();

            if (Objects.requireNonNull(responseDto).getCode() < 0) {
                log.info("Port-One Request Token Error - code: {}, msg: {}", responseDto.getCode(), responseDto.getMessage());
                throw new RuntimeException(); // PortOneGetTokenException;
            }

            return PortOneToken.of(responseDto.getResponse());
        } catch (NullPointerException e) {
            throw new RuntimeException(); // WebClientException;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public PortOnePaymentInfo getPaymentInfo(String token, String impUid) {
        try {
            PortOnePaymentInfoResponseDto responseDto = webClient.get()
                    .uri("/payments/" + impUid)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            clientResponse -> Mono.error(new PortOne4xxResponseException(clientResponse.statusCode().value()))
                    )
                    .bodyToMono(PortOnePaymentInfoResponseDto.class)
                    .block();

            if (Objects.requireNonNull(responseDto).getCode() < 0) {
                log.info("Port-One Request Payment Info Error - code: {}, msg: {}", responseDto.getCode(), responseDto.getMessage());
                throw new RuntimeException(); // PortOneGetTokenException;
            }

            return responseDto.getResponse();

        } catch (NullPointerException e) {
            throw new RuntimeException();
        }
    }



}
