package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.CartItemModifyService;
import com.liberty52.product.service.controller.dto.CartModifyRequestDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class CartItemModifyController {

  private final CartItemModifyService cartItemModifyService;

  @PatchMapping("/carts/custom-products")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void modifyCartItemList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authId, @RequestPart List<CartModifyRequestDto> dto,
      @RequestPart(value = "file",required = false) List<MultipartFile> imageFile) {
    cartItemModifyService.modifyCartItemList(authId,dto,imageFile);
  }

  @PatchMapping("/guest/carts/custom-products")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void modifyGuestCartItemList(@RequestHeader(HttpHeaders.AUTHORIZATION) String guestId, @RequestPart List<CartModifyRequestDto> dto,
      @RequestPart(value = "file",required = false) List<MultipartFile> imageFile) {
    cartItemModifyService.modifyGuestCartItemList(guestId,dto,imageFile);
  }

}