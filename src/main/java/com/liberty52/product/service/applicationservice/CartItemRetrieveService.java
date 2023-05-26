package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.CartItemResponse;

import java.util.List;

public interface CartItemRetrieveService {
    List<CartItemResponse> retrieveAuthCartItem(String authId);

    List<CartItemResponse> retrieveGuestCartItem(String guestId);
}
