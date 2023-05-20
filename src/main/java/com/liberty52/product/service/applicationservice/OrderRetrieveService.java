package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRetrieveService {

    List<OrdersRetrieveResponse> retrieveOrders(String authId);

    OrderDetailRetrieveResponse retrieveOrderDetail(String authId, String orderId);

    OrderDetailRetrieveResponse retrieveGuestOrderDetail(String authId, String orderNumber);

    AdminOrderListResponse retrieveOrdersByAdmin(String role, Pageable pageable);

    OrderDetailRetrieveResponse retrieveOrderDetailByAdmin(String role, String orderId);

    AdminCanceledOrderListResponse retrieveCanceledOrdersByAdmin(String role, Pageable pageable);

    AdminCanceledOrderListResponse retrieveOnlyRequestedCanceledOrdersByAdmin(String role, Pageable pageable);

    AdminCanceledOrderListResponse retrieveOnlyCanceledOrdersByAdmin(String role, Pageable pageable);

    AdminCanceledOrderDetailResponse retrieveCanceledOrderDetailByAdmin(String role, String orderId);

}
