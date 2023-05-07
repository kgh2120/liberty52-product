package com.liberty52.product.service.controller.guest;

import com.liberty52.product.service.applicationservice.OrderCreateService;
import com.liberty52.product.service.controller.dto.PaymentConfirmResponseDto;
import com.liberty52.product.service.controller.dto.PaymentVBankResponseDto;
import com.liberty52.product.service.controller.dto.OrderCreateRequestDto;
import com.liberty52.product.service.controller.dto.PaymentCardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class GuestOrderCreateController {

    private final OrderCreateService orderCreateService;

    @PostMapping("/guest/orders/card")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentCardResponseDto preregisterCardPaymentOrdersByGuest(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String guestId,
            @RequestPart("dto") @Validated OrderCreateRequestDto dto,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        return orderCreateService.createCardPaymentOrders(guestId, dto, imageFile);
    }

    @GetMapping("/guest/orders/card/{orderId}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public PaymentConfirmResponseDto confirmFinalApprovalOfCardPaymentByGuest(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String guestId,
            @PathVariable("orderId") String orderId
    ) {
        return orderCreateService.confirmFinalApprovalOfCardPayment(guestId, orderId);
    }

    @PostMapping("/guest/orders/vbank")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentVBankResponseDto registerVBankPaymentOrdersByGuest(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String guestId,
            @RequestPart("dto") @Validated OrderCreateRequestDto dto,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        return orderCreateService.createVBankPaymentOrders(guestId, dto, imageFile);
    }

    @PostMapping("/guest/orders/card/carts")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentCardResponseDto createCardPaymentOrdersByCarts(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String guestId,
            @RequestBody @Validated OrderCreateRequestDto dto
    ) {
        return orderCreateService.createCardPaymentOrdersByCarts(guestId, dto);
    }

    @PostMapping("/guest/orders/vbank/carts")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentVBankResponseDto createVBankPaymentOrdersByCarts(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String guestId,
            @RequestBody @Validated OrderCreateRequestDto dto
    ) {
        return orderCreateService.createVBankPaymentOrdersByCarts(guestId, dto);
    }

}
