package com.liberty52.product.global.exception.external.badrequest;

public class SameOrderStatusRequestException extends BadRequestException{
  public SameOrderStatusRequestException(){
    super("현재와 같은 상태 변경 요청입니다.");
  }
}
