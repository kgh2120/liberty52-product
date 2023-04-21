package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.CartItemResponse;

import java.util.List;

public interface CartItemRetriveService {
    List<CartItemResponse> retriveAuthCartItem(String authId);

    List<CartItemResponse> retriveGuestCartItem(String guestId);
}
