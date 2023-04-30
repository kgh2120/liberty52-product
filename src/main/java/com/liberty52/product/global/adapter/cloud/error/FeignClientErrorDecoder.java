package com.liberty52.product.global.adapter.cloud.error;

import com.liberty52.product.global.exception.external.internalservererror.InternalServerErrorException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Slf4j
public class FeignClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("ERROR URL: {}", response.request().url());
        HttpStatus status = HttpStatus.valueOf(response.status());
        String responseMessage = deserializeResponseMessage(response);

        if (status.is4xxClientError()) {
            log.error("Feign Client 4xx Error - status: {}, response: {}", status.value(), responseMessage);

            if (status == HttpStatus.UNAUTHORIZED) {
                return new FeignUnauthorizedException();
            }

            return new Feign4xxException();

        } else if (status.is5xxServerError()) {
            log.error("Feign Client 5xx Error - status: {}, response message: {}", status.value(), responseMessage);
            return new Feign5xxException();
        }

        return new FeignClientException();

    }

    private String deserializeResponseMessage(Response response) {
        try {
            return new String(response.body().asInputStream().readAllBytes());
        } catch (IOException e) {
            log.error("Feign Client Error - deserialize response");
            throw new InternalServerErrorException("내부 시스템 사이에 오류가 발생하였습니다.");
        }
    }

}
