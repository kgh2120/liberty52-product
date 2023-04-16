package com.liberty52.product.global.exception.external;

public class CustomProductNotFoundExcpetion extends AbstractApiException{

  public CustomProductNotFoundExcpetion() {
    super(ProductErrorCode.NOT_FOUND_CUSTOM_PRODUCT);
  }
}
