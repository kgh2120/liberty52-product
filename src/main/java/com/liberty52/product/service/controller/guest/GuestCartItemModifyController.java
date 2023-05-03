package com.liberty52.product.service.controller.guest;

import com.liberty52.product.service.applicationservice.CartItemModifyService;
import com.liberty52.product.service.controller.dto.CartModifyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class GuestCartItemModifyController {

    private final CartItemModifyService cartItemModifyService;

    @PatchMapping("/guest/carts/custom-products/{customProductId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyGuestCartItem(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String guestId,
            @RequestPart CartModifyRequestDto dto,
            @RequestPart(value = "file",required = false) MultipartFile imageFile,
            @PathVariable String customProductId
    ) {
        cartItemModifyService.modifyGuestCartItem(guestId,dto,imageFile,customProductId);
    }

}
