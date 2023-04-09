package com.liberty52.product.global.exception.external;

import jakarta.validation.constraints.NotBlank;

public class ProductNotFoundException extends AbstractApiException {
    public ProductNotFoundException(String productId) {
        super(ProductErrorCode.PRODUCT_NOT_FOUND, String.format("productId is not found : "+ productId ));
    }
}
