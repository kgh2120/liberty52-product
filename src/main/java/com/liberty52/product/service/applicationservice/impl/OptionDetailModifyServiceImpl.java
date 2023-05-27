package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.applicationservice.OptionDetailModifyService;
import com.liberty52.product.service.controller.dto.OptionDetailModifyRequestDto;
import com.liberty52.product.service.entity.OptionDetail;
import com.liberty52.product.service.repository.OptionDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OptionDetailModifyServiceImpl implements OptionDetailModifyService {

    private final OptionDetailRepository optionDetailRepository;

    @Override
    public void modifyOptionDetailByAdmin(String role, String optionDetailId, OptionDetailModifyRequestDto dto) {
        Validator.isAdmin(role);
        OptionDetail optionDetail = optionDetailRepository.findById(optionDetailId).orElseThrow(() -> new ResourceNotFoundException("OptionDetail", "ID", optionDetailId));
        optionDetail.modify(dto.getName(), dto.getPrice(), dto.getOnSale());
    }

    public void modifyOptionDetailOnSailStateByAdmin(String role, String optionDetailId) {
        Validator.isAdmin(role);
        OptionDetail optionDetail = optionDetailRepository.findById(optionDetailId).orElseThrow(() -> new ResourceNotFoundException("OptionDetail", "ID", optionDetailId));
        optionDetail.updateOnSale();

    }
}
