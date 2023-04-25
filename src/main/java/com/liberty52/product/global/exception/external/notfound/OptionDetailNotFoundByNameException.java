package com.liberty52.product.global.exception.external.notfound;

public class OptionDetailNotFoundByNameException extends ResourceNotFoundException {
    public OptionDetailNotFoundByNameException(String optionDetailId) {
        super("OptionDetail", "name", optionDetailId);
    }
}
