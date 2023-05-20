package com.liberty52.product.global.adapter.portone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liberty52.product.global.adapter.portone.dto.*;
import com.liberty52.product.global.adapter.portone.exception.PortOne4xxResponseException;
import com.liberty52.product.global.adapter.portone.exception.PortOneErrorException;
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
            PortOneTokenDto.Response responseDto = webClient.post()
                    .uri("/users/getToken")
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(new ObjectMapper().writeValueAsString(
                            PortOneTokenDto.Request.of(PORT_ONE_API_KEY, PORT_ONE_SECRET_KEY)
                    ))
                    .retrieve()
                    .bodyToMono(PortOneTokenDto.Response.class)
                    .block();

            if (Objects.requireNonNull(responseDto).getCode() != 0) {
                log.error("Port-One Request Token Error - code={}, msg={}", responseDto.getCode(), responseDto.getMessage());
                throw new PortOneErrorException("API 토큰 발급 응답 실패");
            }
            return PortOneToken.of(responseDto.getResponse());

        } catch (NullPointerException | JsonProcessingException e) {
            log.error("Port-One Request Token Error - ", e);
            throw new PortOneErrorException("API 토큰 발급 내부 실패");
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

            if (Objects.requireNonNull(responseDto).getCode() != 0) {
                log.error("Port-One Request Payment Info Error - code={}, msg={}", responseDto.getCode(), responseDto.getMessage());
                throw new PortOneErrorException("결제 정보 요청 응답 실패");
            }
            return responseDto.getResponse();

        } catch (NullPointerException e) {
            log.error("Port-One Request Payment Info Error - ", e);
            throw new PortOneErrorException("결제 정보 요청 내부 실패");
        }
    }

    public void requestCancelPayments(String token, PortOneCancelDto.Request requestDto) {
        try {
            PortOneCancelDto.Response response = webClient.post()
                    .uri("/payments/cancel")
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(new ObjectMapper().writeValueAsString(requestDto))
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            clientResponse -> Mono.error(new PortOne4xxResponseException(clientResponse.statusCode().value()))
                    )
                    .bodyToMono(PortOneCancelDto.Response.class)
                    .block();

            if (Objects.requireNonNull(response).getCode() != 0) {
                log.error("Port-One Request Cancel Payments Error - code={}, msg={}", response.getCode(), response.getMessage());
                throw new PortOneErrorException("결제 취소 요청 응답 실패");
            }

        } catch (NullPointerException | JsonProcessingException e) {
            log.error("Port-One Request Cancel Payments Error - ", e);
            throw new PortOneErrorException("결제 취소 요청 내부 실패");
        }
    }

}
