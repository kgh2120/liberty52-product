package com.liberty52.product.service.controller.guest;

import com.liberty52.product.service.applicationservice.CartItemRemoveService;
import com.liberty52.product.service.controller.dto.CartItemListRemoveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GuestCartItemRemoveController {

    private final CartItemRemoveService cartItemRemoveService;

    @DeleteMapping("/guest/carts/custom-products/{customProductId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void guestCartItemRemove(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String guestId,
            @PathVariable String customProductId
    ) {
        cartItemRemoveService.removeGuestCartItem(guestId, customProductId);
    }

    @DeleteMapping("/guest/carts/custom-products")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void guestCartItemListRemove(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String guestId,
            @RequestBody @Validated CartItemListRemoveRequestDto dto
    ) {
        cartItemRemoveService.removeGuestCartItemList(guestId, dto);
    }

}
