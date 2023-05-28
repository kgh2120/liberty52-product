package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.service.applicationservice.VBankRetrieveService;
import com.liberty52.product.service.controller.dto.VBankDto;
import com.liberty52.product.service.entity.payment.VBank;
import com.liberty52.product.service.repository.VBankRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VBankRetrieveServiceImpl implements VBankRetrieveService {

    private final VBankRepository vBankRepository;

    @Override
    public List<VBankDto> getVBankList() {
        List<VBank> vbanks = vBankRepository.findAll();

        return vbanks.stream()
                .map(VBankDto::fromEntity)
                .toList();
    }
}
