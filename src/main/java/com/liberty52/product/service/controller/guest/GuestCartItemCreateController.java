package com.liberty52.product.service.controller.guest;

import com.liberty52.product.service.applicationservice.CartItemCreateService;
import com.liberty52.product.service.controller.dto.CartItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class GuestCartItemCreateController {

    private final CartItemCreateService cartItemCreateService;

    @PostMapping("/guest/carts/custom-products")
    @ResponseStatus(HttpStatus.CREATED)
    public void createGuestCartItem(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String guestId,
            @RequestPart(value = "file") MultipartFile imageFile,
            @RequestPart CartItemRequest dto
    ) {
        cartItemCreateService.createGuestCartItem(guestId, imageFile, dto);
    }

}
