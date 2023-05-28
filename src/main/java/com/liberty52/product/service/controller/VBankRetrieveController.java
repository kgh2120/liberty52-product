package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.VBankRetrieveService;
import com.liberty52.product.service.controller.dto.VBankRetrieve;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VBankRetrieveController {

    private final VBankRetrieveService vBankRetrieveService;

    @GetMapping("/vbanks")
    @ResponseStatus(HttpStatus.OK)
    public VBankRetrieve.ListResponse getVBankInfoList() {
        return VBankRetrieve.ListResponse.fromDtoList(
                vBankRetrieveService.getVBankList()
        );
    }

}
