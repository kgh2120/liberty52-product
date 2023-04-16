package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.CartModifyRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface CartItemModifyService {
  void modifyCartItem(String authId, CartModifyRequestDto dto, MultipartFile imageFile, String customProductId);
}
