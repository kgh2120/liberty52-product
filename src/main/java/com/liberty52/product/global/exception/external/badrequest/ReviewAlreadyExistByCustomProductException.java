package com.liberty52.product.global.exception.external.badrequest;

public class ReviewAlreadyExistByCustomProductException extends BadRequestException {

  public ReviewAlreadyExistByCustomProductException() {
    super("이미 제품에 대한 리뷰가 존재합니다.");
  }
}
