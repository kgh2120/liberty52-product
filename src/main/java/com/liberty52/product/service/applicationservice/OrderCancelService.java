package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.OrderCancelDto;

public interface OrderCancelService {

    OrderCancelDto.Response cancelOrder(String authId, OrderCancelDto.Request request);

}
