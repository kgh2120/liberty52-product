package com.liberty52.product.global.exception.external.badrequest;

public class AbnormalOrderStatusRequestException extends BadRequestException{
  public AbnormalOrderStatusRequestException(){
    super("비정상적인 상태 변경 요청입니다.");
  }
}
