package com.liberty52.product.global.exception.external;

public class OrderNotFoundException extends AbstractApiException {
  public OrderNotFoundException() {
    super(ProductErrorCode.ORDER_NOT_FOUND, "주문정보가 존재하지 않습니다");
  }
}
