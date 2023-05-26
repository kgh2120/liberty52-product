package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.applicationservice.OptionDetailRemoveService;
import com.liberty52.product.service.controller.dto.OptionDetailRemoveRequestDto;
import com.liberty52.product.service.entity.OptionDetail;
import com.liberty52.product.service.repository.OptionDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OptionDetailRemoveServiceImpl implements OptionDetailRemoveService {

    private final OptionDetailRepository optionDetailRepository;

    @Override
    public void removeOptionDetailByAdmin(String role, String optionDetailId, OptionDetailRemoveRequestDto dto) {
      Validator.isAdmin(role);
        OptionDetail optionDetail = optionDetailRepository.findById(optionDetailId).orElseThrow(() -> new ResourceNotFoundException("OptionDetail", "ID", optionDetailId));
        optionDetail.updateOnSale(dto.getOnSail());
    }
}
