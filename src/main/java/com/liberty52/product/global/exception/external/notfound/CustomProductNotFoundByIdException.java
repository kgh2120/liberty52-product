package com.liberty52.product.global.exception.external.notfound;

public class CustomProductNotFoundByIdException extends ResourceNotFoundException {

  public CustomProductNotFoundByIdException(String id) {
    super("CustomProduct", "id", id);
  }
}
