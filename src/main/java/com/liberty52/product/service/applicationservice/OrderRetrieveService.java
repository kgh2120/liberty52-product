package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.OrderDetailRetrieveResponse;
import com.liberty52.product.service.controller.dto.OrdersRetrieveResponse;
import java.util.List;

public interface OrderRetrieveService {

    List<OrdersRetrieveResponse> retrieveOrders(String authId);

     OrderDetailRetrieveResponse retrieveOrderDetail(String authId, String orderId);
}
