package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.CartItemModifyService;
import com.liberty52.product.service.controller.dto.CartModifyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class CartItemModifyController {

  private final CartItemModifyService cartItemModifyService;

  @PatchMapping("/carts/custom-products/{customProductId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void modifyCartItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String authId, @RequestPart CartModifyRequestDto dto,
      @RequestPart(value = "file",required = false) MultipartFile imageFile, @PathVariable String customProductId) {
    cartItemModifyService.modifyUserCartItem(authId,dto,imageFile,customProductId);
  }

}