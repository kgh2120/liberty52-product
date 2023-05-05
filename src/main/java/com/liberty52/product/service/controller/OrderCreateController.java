package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.OrderCreateService;
import com.liberty52.product.service.controller.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class OrderCreateController {
    private final OrderCreateService orderCreateService;

    @Deprecated
    @PostMapping("/orders/custom-products")
    @ResponseStatus(HttpStatus.CREATED)
    public MonoItemOrderResponseDto orderSave(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
            @RequestPart("imageFile") MultipartFile imageFile,
            @RequestPart("dto") @Validated MonoItemOrderRequestDto dto) {
        return orderCreateService.save(authId, imageFile, dto);
    }

    // /orders/card
    @PostMapping("/orders/payment/card/prepare")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentCardResponseDto createCardPaymentOrders(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
            @RequestPart("dto") @Validated OrderCreateRequestDto dto,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        return orderCreateService.createCardPaymentOrders(authId, dto, imageFile);
    }

    // orders/card/{orderId}/confirm
    @GetMapping("/orders/payment/card/confirm/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentConfirmResponseDto confirmFinalApprovalOfCardPayment(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
            @PathVariable("orderId") String orderId
    ) {
        return orderCreateService.confirmFinalApprovalOfCardPayment(authId, orderId);
    }

    // /orders/vbank
    @PostMapping("/orders/payment/vbank")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentVBankResponseDto createVBankPaymentOrders(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
            @RequestPart("dto") @Validated OrderCreateRequestDto dto,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        return orderCreateService.createVBankPaymentOrders(authId, dto, imageFile);
    }

    // /orders/card/carts
    @PostMapping("/orders/payment/card/prepare/carts")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentCardResponseDto createCardPaymentOrdersByCarts(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
            @RequestBody @Validated OrderCreateRequestDto dto
    ) {
        return orderCreateService.createCardPaymentOrdersByCarts(authId, dto);
    }

    // /orders/vbank/carts
    @PostMapping("/orders/payment/vbank/carts")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentVBankResponseDto createVBankPaymentOrdersByCarts(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
            @RequestBody @Validated OrderCreateRequestDto dto
    ) {
        return orderCreateService.createVBankPaymentOrdersByCarts(authId, dto);
    }

}
