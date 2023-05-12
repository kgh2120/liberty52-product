package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.OrderRetrieveService;
import com.liberty52.product.service.controller.dto.AdminOrderListResponse;
import com.liberty52.product.service.controller.dto.OrderDetailRetrieveResponse;
import com.liberty52.product.service.controller.dto.OrdersRetrieveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrderRetrieveController {

    private final OrderRetrieveService orderRetrieveService;

    @GetMapping("/orders")
    public ResponseEntity<List<OrdersRetrieveResponse>> retrieveOrders(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return ResponseEntity.ok(orderRetrieveService.retrieveOrders(authorization));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDetailRetrieveResponse> retrieveOrderDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable("orderId") String orderId){
        return ResponseEntity.ok(orderRetrieveService.retrieveOrderDetail(authorization,orderId));
    }

    @GetMapping("/admin/orders")
    @ResponseStatus(HttpStatus.OK)
    public AdminOrderListResponse retrieveOrdersByAdmin(
            @RequestHeader("LB-Role") String role,
            Pageable pageable
    ) {
        return orderRetrieveService.retrieveOrdersByAdmin(role, pageable);
    }

}
