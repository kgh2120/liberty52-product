package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.CartItemCreateService;
import com.liberty52.product.service.controller.dto.CartItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class CartItemCreateController {
    private final CartItemCreateService cartItemCreateService;

    @PostMapping("/cart/custom-product")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCartItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String authId, @RequestPart(value = "file") MultipartFile imageFile, @RequestBody CartItemRequest dto) {
        cartItemCreateService.init();
        cartItemCreateService.createCartItem(authId, imageFile, dto);
    }

}
