package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.badrequest.BadRequestException;
import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.applicationservice.VBankDeleteService;
import com.liberty52.product.service.entity.payment.VBank;
import com.liberty52.product.service.repository.VBankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VBankDeleteServiceImpl implements VBankDeleteService {

    private final VBankRepository vBankRepository;

    @Override
    @Transactional
    public void deleteVBankByAdmin(String role, String vBankId) {
        Validator.isAdmin(role);

        VBank vBank = vBankRepository.findById(vBankId)
                .orElseThrow(() -> new ResourceNotFoundException("VBANK", "id", vBankId));

        validateDeleteVBank();

        vBankRepository.delete(vBank);
    }

    private void validateDeleteVBank() {
        if (vBankRepository.countAll() <= 1L) {
            throw new BadRequestException("가상계좌는 반드시 1개 이상 존재해야 합니다.");
        }
    }
}
