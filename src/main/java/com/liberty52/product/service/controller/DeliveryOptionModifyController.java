package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.DeliveryOptionModifyService;
import com.liberty52.product.service.controller.dto.DeliveryOptionFeeModify;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DeliveryOptionModifyController {

    private final DeliveryOptionModifyService deliveryOptionModifyService;

    @PatchMapping("/admin/options/delivery/fee")
    @ResponseStatus(HttpStatus.OK)
    public DeliveryOptionFeeModify.Response updateDefaultDeliveryFeeByAdmin(@RequestHeader(HttpHeaders.AUTHORIZATION) String adminId,
                                                                            @RequestHeader("LB-Role") String role,
                                                                            @RequestBody @Valid DeliveryOptionFeeModify.Request request) {
        return DeliveryOptionFeeModify.Response.fromDto(
                deliveryOptionModifyService.updateDefaultDeliveryFeeByAdmin(role, request.getFee())
        );
    }
}
