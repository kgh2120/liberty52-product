package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.CartItemRemoveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartItemRemoveController {
    private final CartItemRemoveService cartItemRemoveService;

    @DeleteMapping("/cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cartItemRemove(@RequestHeader(HttpHeaders.AUTHORIZATION) String authId, @PathVariable String cartItemId) {
        cartItemRemoveService.removeCartItem(authId, cartItemId);
    }
}
