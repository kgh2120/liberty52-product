package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.VBankDto;

public interface VBankCreateService {

    VBankDto createVBankByAdmin(String role, String bank, String account, String holder);

}
