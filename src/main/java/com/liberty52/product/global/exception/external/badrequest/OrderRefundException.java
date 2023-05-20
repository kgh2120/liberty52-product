package com.liberty52.product.global.exception.external.badrequest;

import com.liberty52.product.service.entity.OrderStatus;

public class OrderRefundException extends BadRequestException {
    public OrderRefundException(OrderStatus nowStatus) {
        super("해당 주문은 환불 처리할 수 없는 상태입니다. 현재상태: "+nowStatus.name());
    }

    public OrderRefundException(String msg) {
        super(msg);
    }
}
