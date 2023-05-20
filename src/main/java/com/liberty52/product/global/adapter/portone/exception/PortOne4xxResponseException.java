package com.liberty52.product.global.adapter.portone.exception;

public class PortOne4xxResponseException extends PortOneErrorException {
    public PortOne4xxResponseException(int httpStatusCode) {
        super(String.format("occurred 4xx Error - code: %d", httpStatusCode));
    }
}
