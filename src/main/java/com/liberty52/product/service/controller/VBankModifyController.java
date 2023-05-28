package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.VBankModifyService;
import com.liberty52.product.service.controller.dto.VBankModify;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class VBankModifyController {

    private final VBankModifyService vBankModifyService;

    @PutMapping("/admin/vbanks/{vBankId}")
    @ResponseStatus(HttpStatus.OK)
    public VBankModify.Response updateVBankByAdmin(@RequestHeader(HttpHeaders.AUTHORIZATION) String adminId,
                                                   @RequestHeader("LB-Role") String role,
                                                   @PathVariable String vBankId,
                                                   @RequestBody @Valid VBankModify.Request request) {
        return VBankModify.Response.fromDto(
                vBankModifyService.updateVBankByAdmin(role, vBankId, request.getBank(), request.getAccountNumber(), request.getHolder())
        );
    }
}
