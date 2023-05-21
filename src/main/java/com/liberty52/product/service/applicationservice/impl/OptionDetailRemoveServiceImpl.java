package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.forbidden.InvalidRoleException;
import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.service.applicationservice.OptionDetailRemoveService;
import com.liberty52.product.service.controller.dto.OptionDetailRemoveRequestDto;
import com.liberty52.product.service.entity.OptionDetail;
import com.liberty52.product.service.entity.Review;
import com.liberty52.product.service.repository.OptionDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.liberty52.product.global.contants.RoleConstants.ADMIN;

@Service
@Transactional
@RequiredArgsConstructor
public class OptionDetailRemoveServiceImpl implements OptionDetailRemoveService {

    private final OptionDetailRepository optionDetailRepository;

    @Override
    public void removeOptionDetail(String role, String optionDetailId, OptionDetailRemoveRequestDto dto) {
        if(!ADMIN.equals(role)) {
            throw new InvalidRoleException(role);
        }
        OptionDetail optionDetail = optionDetailRepository.findById(optionDetailId).orElseThrow(() -> new ResourceNotFoundException("OptionDetail", "ID", optionDetailId));
        optionDetail.updateOnSale(dto.getOnSail());
    }
}
