package com.liberty52.product.global.exception.external.notfound;

public class ProductNotFoundByNameException extends ResourceNotFoundException {
    public ProductNotFoundByNameException(String productName) {
        super("Product", "name", productName);
    }
}
