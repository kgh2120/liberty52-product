package com.liberty52.product.service.controller.guest;

import com.liberty52.product.service.applicationservice.OrderRetrieveService;
import com.liberty52.product.service.controller.dto.OrderDetailRetrieveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GuestOrderRetrieveController {

    private final OrderRetrieveService orderRetrieveService;

    @GetMapping("/guest/orders/{orderId}")
    public ResponseEntity<OrderDetailRetrieveResponse> retrieveGuestOrderDetail(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String guestId,
        @PathVariable("orderId") String orderId
    ){
        return ResponseEntity.ok(orderRetrieveService.retrieveOrderDetail(guestId,orderId));
    }

}
