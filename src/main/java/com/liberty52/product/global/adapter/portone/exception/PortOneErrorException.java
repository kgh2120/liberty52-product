package com.liberty52.product.global.adapter.portone.exception;

import com.liberty52.product.global.exception.external.internalservererror.InternalServerErrorException;

public class PortOneErrorException extends InternalServerErrorException {
    public PortOneErrorException(String msg) {
        super("PortOne "+msg);
    }
}
