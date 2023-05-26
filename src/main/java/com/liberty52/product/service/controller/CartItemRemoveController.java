package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.CartItemRemoveService;
import com.liberty52.product.service.controller.dto.CartItemListRemoveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartItemRemoveController {
    private final CartItemRemoveService cartItemRemoveService;

    @DeleteMapping("/carts/custom-products/{customProductId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCartItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String authId, @PathVariable String customProductId) {
        cartItemRemoveService.removeCartItem(authId, customProductId);
    }

    @DeleteMapping("/carts/custom-products")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCartItemList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
                                   @RequestBody @Validated CartItemListRemoveRequestDto dto) {
        cartItemRemoveService.removeCartItemList(authId, dto);
    }

}
