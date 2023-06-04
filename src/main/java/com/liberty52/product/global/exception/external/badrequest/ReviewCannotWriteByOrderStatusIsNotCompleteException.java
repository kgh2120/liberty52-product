package com.liberty52.product.global.exception.external.badrequest;

public class ReviewCannotWriteByOrderStatusIsNotCompleteException extends BadRequestException {

  public ReviewCannotWriteByOrderStatusIsNotCompleteException() {
    super("배송 완료된 제품에만 리뷰를 남길 수 있습니다");
  }
}
