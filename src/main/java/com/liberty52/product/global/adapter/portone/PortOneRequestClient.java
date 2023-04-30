package com.liberty52.product.global.adapter.portone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liberty52.product.global.adapter.portone.dto.*;
import com.liberty52.product.global.adapter.portone.exception.PortOne4xxResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${portone.api-key}")
    private String PORT_ONE_API_KEY;
    @Value("${portone.secret-key}")
    private String PORT_ONE_SECRET_KEY;
    private final WebClient webClient;

    public PortOneToken getAccessToken() {
        try {
            PortOneTokenResponseDto responseDto = webClient.post()
                    .uri("/users/getToken")
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(new ObjectMapper().writeValueAsString(
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
