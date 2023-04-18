package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.CartItemListRemoveRequestDto;

public interface CartItemRemoveService {
    void removeCartItem(String authId, String cartItemId);

    void removeCartItemList(String authId, CartItemListRemoveRequestDto dto);
}
