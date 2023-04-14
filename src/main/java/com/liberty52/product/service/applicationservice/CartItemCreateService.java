package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.CartItemRequest;
import org.springframework.web.multipart.MultipartFile;

public interface CartItemCreateService {
    void createCartItem(String authId, MultipartFile imageFile, CartItemRequest dto);

}
