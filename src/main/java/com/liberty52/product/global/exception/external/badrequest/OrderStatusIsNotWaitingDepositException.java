package com.liberty52.product.global.exception.external.badrequest;

public class OrderStatusIsNotWaitingDepositException extends BadRequestException{
  public OrderStatusIsNotWaitingDepositException(){
    super("입금대기 상태가 아닙니다.");
  }
}
