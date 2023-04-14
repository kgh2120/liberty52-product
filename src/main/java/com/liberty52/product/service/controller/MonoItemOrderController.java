package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.MonoItemOrderService;
import com.liberty52.product.service.controller.dto.MonoItemOrderRequestDto;
import com.liberty52.product.service.controller.dto.MonoItemOrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MonoItemOrderController {
    private final MonoItemOrderService monoItemOrderService;

    @PostMapping("/order/custom-product")
    @ResponseStatus(HttpStatus.CREATED)
    public MonoItemOrderResponseDto orderSave(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authId,
            @RequestPart("imageFile") MultipartFile imageFile,
            @RequestPart("dto") @Validated MonoItemOrderRequestDto dto) {
        return monoItemOrderService.save(authId, imageFile, dto);
    }
}
