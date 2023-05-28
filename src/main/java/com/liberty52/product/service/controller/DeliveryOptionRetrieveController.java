package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.DeliveryOptionRetrieveService;
import com.liberty52.product.service.controller.dto.DeliveryOptionFeeRetrieve;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeliveryOptionRetrieveController {

    private final DeliveryOptionRetrieveService deliveryOptionRetrieveService;

    @GetMapping("/options/delivery/fee")
    @ResponseStatus(HttpStatus.OK)
    public DeliveryOptionFeeRetrieve.Response getDefaultDeliveryFee() {
        return DeliveryOptionFeeRetrieve.Response.fromDto(
                deliveryOptionRetrieveService.getDefaultDeliveryFee()
        );
    }

}
