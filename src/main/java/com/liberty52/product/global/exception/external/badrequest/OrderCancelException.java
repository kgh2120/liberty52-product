package com.liberty52.product.global.exception.external.badrequest;

import com.liberty52.product.service.entity.OrderStatus;

public class OrderCancelException extends BadRequestException {
    public OrderCancelException(OrderStatus nowStatus) {
        super("해당 주문은 취소할 수 없는 상태입니다. 현재상태: " + nowStatus.name());
    }
}
