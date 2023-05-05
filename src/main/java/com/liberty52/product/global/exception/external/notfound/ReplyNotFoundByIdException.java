package com.liberty52.product.global.exception.external.notfound;

public class ReplyNotFoundByIdException extends ResourceNotFoundException {
    public ReplyNotFoundByIdException(String replyId) {
        super("Reply", "id", replyId);
    }
}
