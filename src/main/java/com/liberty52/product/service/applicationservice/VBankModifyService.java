package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.VBankDto;

public interface VBankModifyService {

    VBankDto updateVBankByAdmin(String role, String vBankId, String bank, String accountNumber, String holder);

}
