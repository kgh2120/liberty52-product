package com.liberty52.product.global.exception.external;

import static com.liberty52.product.global.exception.external.ProductErrorCode.ALREADY_REVIEW_EXIST_BY_ORDER;

public class ReviewAlreadyExistByOrderException extends AbstractApiException{

  public ReviewAlreadyExistByOrderException() {
    super(ALREADY_REVIEW_EXIST_BY_ORDER);
  }
}
