package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.applicationservice.VBankModifyService;
import com.liberty52.product.service.controller.dto.VBankDto;
import com.liberty52.product.service.entity.payment.BankType;
import com.liberty52.product.service.entity.payment.VBank;
import com.liberty52.product.service.repository.VBankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class VBankModifyServiceImpl implements VBankModifyService {

    private final VBankRepository vBankRepository;

    @Override
    @Transactional
    public VBankDto updateVBankByAdmin(String role, String vBankId, String bank, String accountNumber, String holder) {
        Validator.isAdmin(role);

        VBank vBank = vBankRepository.findById(vBankId)
                .orElseThrow(() -> new ResourceNotFoundException("VBANK", "id", vBankId));

        vBank.update(BankType.getBankType(bank), accountNumber, holder);

        return VBankDto.fromEntity(vBank);
    }

}
