package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.VBankCreateService;
import com.liberty52.product.service.controller.dto.VBankCreate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class VBankCreateController {

    private final VBankCreateService vBankCreateService;

    @PostMapping("/admin/vbanks")
    @ResponseStatus(HttpStatus.CREATED)
    public VBankCreate.Response createVBankByAdmin(@RequestHeader(HttpHeaders.AUTHORIZATION) String adminId,
                                                   @RequestHeader("LB-Role") String role,
                                                   @RequestBody @Valid VBankCreate.Request request) {
        return VBankCreate.Response.fromDto(
            vBankCreateService.createVBankByAdmin(role, request.getBank(), request.getAccountNumber(), request.getHolder())
        );
    }

}
