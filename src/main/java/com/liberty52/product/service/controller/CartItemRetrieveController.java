package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.CartItemRetrieveService;
import com.liberty52.product.service.controller.dto.CartItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartItemRetrieveController {

    private final CartItemRetrieveService cartItemRetrieveService;

    @GetMapping("/carts")
    @ResponseStatus(HttpStatus.OK)
    public List<CartItemResponse> retrieveAuthCartItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String authId) {
        return cartItemRetrieveService.retrieveAuthCartItem(authId);
    }

}
