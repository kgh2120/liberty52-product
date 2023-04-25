package com.liberty52.product.global.exception.external.notfound;

public class OrderNotFoundByIdException extends ResourceNotFoundException {
  public OrderNotFoundByIdException(String orderId) {
    super("Order", "id", orderId);
  }
}
