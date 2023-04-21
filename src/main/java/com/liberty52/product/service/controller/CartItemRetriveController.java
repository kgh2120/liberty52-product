package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.CartItemRetriveService;
import com.liberty52.product.service.controller.dto.CartItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartItemRetriveController {

    private final CartItemRetriveService cartItemRetriveService;

    @GetMapping("/carts")
    @ResponseStatus(HttpStatus.OK)
    public List<CartItemResponse> retriveAuthCartItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String authId) {
        return cartItemRetriveService.retriveAuthCartItem(authId);
    }

    @GetMapping("/guest/carts")
    @ResponseStatus(HttpStatus.OK)
    public List<CartItemResponse> retriveGuestCartItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String guestId) {
        return cartItemRetriveService.retriveGuestCartItem(guestId);
    }
}
