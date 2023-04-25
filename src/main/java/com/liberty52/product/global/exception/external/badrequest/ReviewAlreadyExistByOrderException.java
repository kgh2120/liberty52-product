package com.liberty52.product.global.exception.external.badrequest;

import com.liberty52.product.global.exception.external.badrequest.BadRequestException;

public class ReviewAlreadyExistByOrderException extends BadRequestException {

  public ReviewAlreadyExistByOrderException() {
    super("이미 제품에 대한 리뷰가 존재합니다.");
  }
}
