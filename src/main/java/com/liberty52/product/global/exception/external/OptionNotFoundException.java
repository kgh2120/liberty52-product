package com.liberty52.product.global.exception.external;

import jakarta.validation.constraints.NotBlank;

public class OptionNotFoundException extends AbstractApiException {
    public OptionNotFoundException(String optionId) {
        super(ProductErrorCode.OPTION_NOT_FOUND, String.format("optionId is not found : "+ optionId ));
    }
}
