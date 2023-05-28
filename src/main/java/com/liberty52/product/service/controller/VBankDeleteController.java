package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.VBankDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class VBankDeleteController {

    private final VBankDeleteService vBankDeleteService;

    @DeleteMapping("/admin/vbanks/{vBankId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteVBankByAdmin(@RequestHeader(HttpHeaders.AUTHORIZATION) String adminId,
                                   @RequestHeader("LB-Role") String role,
                                   @PathVariable String vBankId) {
        vBankDeleteService.deleteVBankByAdmin(role, vBankId);
    }

}
