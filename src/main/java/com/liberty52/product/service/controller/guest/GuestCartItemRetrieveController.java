package com.liberty52.product.service.controller.guest;

import com.liberty52.product.service.applicationservice.CartItemRetrieveService;
import com.liberty52.product.service.controller.dto.CartItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GuestCartItemRetrieveController {

    private final CartItemRetrieveService cartItemRetrieveService;

    @GetMapping("/guest/carts")
    @ResponseStatus(HttpStatus.OK)
    public List<CartItemResponse> retrieveGuestCartItem(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String guestId
    ) {
        return cartItemRetrieveService.retrieveGuestCartItem(guestId);
    }

}
