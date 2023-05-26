package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.OrderCancelDto;
import com.liberty52.product.service.controller.dto.OrderRefundDto;

public interface OrderCancelService {

    OrderCancelDto.Response cancelOrder(String authId, OrderCancelDto.Request request);

    void refundCustomerOrderByAdmin(String adminId, String role, OrderRefundDto.Request request);

}
