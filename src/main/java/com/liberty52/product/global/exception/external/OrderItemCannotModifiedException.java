package com.liberty52.product.global.exception.external;

public class OrderItemCannotModifiedException extends AbstractApiException{

  public OrderItemCannotModifiedException() {
    super(ProductErrorCode.BAD_REQUEST);
  }
}
