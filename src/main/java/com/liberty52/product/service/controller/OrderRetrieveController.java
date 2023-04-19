package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.OrderRetrieveService;
import com.liberty52.product.service.controller.dto.OrderDetailRetrieveResponse;
import com.liberty52.product.service.controller.dto.OrdersRetrieveResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/guest/orders/{orderId}")
    public ResponseEntity<OrderDetailRetrieveResponse> retrieveGuestOrderDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String guestId,
            @PathVariable("orderId") String orderId){
        return ResponseEntity.ok(orderRetrieveService.retrieveOrderDetail(guestId,orderId));
    }

}
