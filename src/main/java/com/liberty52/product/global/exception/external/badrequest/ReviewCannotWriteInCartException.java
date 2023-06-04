package com.liberty52.product.global.exception.external.badrequest;

public class ReviewCannotWriteInCartException extends BadRequestException {

  public ReviewCannotWriteInCartException() {
    super("구매한 제품에만 리뷰를 남길 수 있습니다");
  }
}
