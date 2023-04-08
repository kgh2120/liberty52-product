package com.liberty52.product.global.exception.external;

import jakarta.validation.constraints.NotBlank;

public class OptionDetailNotFoundException extends AbstractApiException {
    public OptionDetailNotFoundException(String optionDetailId) {
        super(ProductErrorCode.OPTION_DETAIL_NOT_FOUND, String.format("optionDetailId is not found : "+ optionDetailId ));
    }
}
