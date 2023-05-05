package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.VBankInfoRetrieveService;
import com.liberty52.product.service.controller.dto.VBankInfoListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VBankInfoRetrieveController {

    private final VBankInfoRetrieveService vBankInfoRetrieveService;

    // /vbanks
    @GetMapping("/orders/payment/vbank")
    @ResponseStatus(HttpStatus.OK)
    public VBankInfoListResponseDto getVBankInfoList() {
        return vBankInfoRetrieveService.getVBankInfoList();
    }

}
