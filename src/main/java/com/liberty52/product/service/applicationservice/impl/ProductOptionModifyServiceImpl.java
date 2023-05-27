package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.applicationservice.ProductOptionModifyService;
import com.liberty52.product.service.controller.dto.ProductOptionModifyRequestDto;
import com.liberty52.product.service.controller.dto.ProductOptionOnSailModifyRequestDto;
import com.liberty52.product.service.entity.ProductOption;
import com.liberty52.product.service.repository.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductOptionModifyServiceImpl implements ProductOptionModifyService {

    private final ProductOptionRepository productOptionRepository;

    @Override
    public void modifyProductOptionByAdmin(String role, String productOptionId, ProductOptionModifyRequestDto dto) {
        Validator.isAdmin(role);
        ProductOption productOption = productOptionRepository.findById(productOptionId).orElseThrow(() -> new ResourceNotFoundException("ProductOption", "ID", productOptionId));
        productOption.modify(dto.getName(), dto.getRequire(), dto.getOnSale());
    }

    @Override
    public void modifyProductOptionOnSailStateByAdmin(String role, String productOptionId) {
        Validator.isAdmin(role);
        ProductOption productOption = productOptionRepository.findById(productOptionId).orElseThrow(() -> new ResourceNotFoundException("ProductOption", "ID", productOptionId));
        productOption.updateOnSale();
    }
}
