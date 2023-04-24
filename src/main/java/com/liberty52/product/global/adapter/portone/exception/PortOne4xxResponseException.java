package com.liberty52.product.global.adapter.portone.exception;

public class PortOne4xxResponseException extends RuntimeException {
    public PortOne4xxResponseException(int httpStatusCode) {
        super(String.format("Web Client Response 4xx Error - code: %d", httpStatusCode));
    }
}
