package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.service.applicationservice.VBankInfoRetrieveService;
import com.liberty52.product.service.controller.dto.VBankInfoListResponseDto;
import com.liberty52.product.service.entity.payment.VBank;
import com.liberty52.product.service.repository.VBankRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VBankInfoRetrieveServiceImpl implements VBankInfoRetrieveService {

    private final VBankRepository vBankRepository;

    @Override
    public VBankInfoListResponseDto getVBankInfoList() {
        List<VBank> vbanks = vBankRepository.findAll();
        return VBankInfoListResponseDto.of(
                vbanks.stream().map(vBank -> VBankInfoListResponseDto.VBankInfoDto.of(vBank.getAccount())).toList()
        );
    }
}
