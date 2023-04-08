package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.CartItemRetriveService;
import com.liberty52.product.service.controller.dto.CartItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartItemRetriveController {

    private final CartItemRetriveService cartItemRetriveService;

    @GetMapping("/cart")
    @ResponseStatus(HttpStatus.OK)
    public List<CartItemResponse> retriveCartItem(@PathVariable("authId") String AuthId) {
        return cartItemRetriveService.retriveCartItem(AuthId);
    }
}
