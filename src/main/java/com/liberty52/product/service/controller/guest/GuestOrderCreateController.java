package com.liberty52.product.service.controller.guest;

import com.liberty52.product.service.applicationservice.OrderCreateService;
import com.liberty52.product.service.controller.dto.PaymentConfirmResponseDto;
import com.liberty52.product.service.controller.dto.PaymentVBankResponseDto;
import com.liberty52.product.service.controller.dto.PreregisterOrderRequestDto;
import com.liberty52.product.service.controller.dto.PreregisterOrderResponseDto;
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

    @PostMapping("/guest/orders/payment/card/prepare")
    @ResponseStatus(HttpStatus.CREATED)
    public PreregisterOrderResponseDto preregisterCardPaymentOrdersByGuest(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String guestId,
            @RequestPart("dto") @Validated PreregisterOrderRequestDto dto,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        return orderCreateService.preregisterCardPaymentOrders(guestId, dto, imageFile);
    }

    @GetMapping("/guest/orders/payment/card/confirm/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentConfirmResponseDto confirmFinalApprovalOfCardPaymentByGuest(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String guestId,
            @PathVariable("orderId") String orderId
    ) {
        return orderCreateService.confirmFinalApprovalOfCardPayment(guestId, orderId);
    }

    @PostMapping("/guest/orders/payment/vbank")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentVBankResponseDto registerVBankPaymentOrdersByGuest(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String guestId,
            @RequestPart("dto") @Validated PreregisterOrderRequestDto dto,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        return orderCreateService.registerVBankPaymentOrders(guestId, dto, imageFile);
    }

}
