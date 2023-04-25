package com.liberty52.product.global.exception.external.notfound;

import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;

public class ReviewNotFoundByIdException extends ResourceNotFoundException {

    public ReviewNotFoundByIdException(String id) {
        super("Review", "id", id);
    }
}
