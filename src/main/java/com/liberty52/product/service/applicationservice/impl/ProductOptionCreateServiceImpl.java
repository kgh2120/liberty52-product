package com.liberty52.product.service.applicationservice.impl;

import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.global.util.Validator;
import com.liberty52.product.service.applicationservice.ProductOptionCreateService;
import com.liberty52.product.service.controller.dto.CreateProductOptionRequestDto;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.ProductOption;
import com.liberty52.product.service.repository.ProductOptionRepository;
import com.liberty52.product.service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductOptionCreateServiceImpl implements ProductOptionCreateService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;;

    @Override
    public void createProductOptionByAdmin(String role, CreateProductOptionRequestDto dto, String productId) {
        Validator.isAdmin(role);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("productId", "id", productId));
        ProductOption productOption = ProductOption.create(dto.getName(), dto.getRequire(), dto.getOnSale());
        productOption.associate(product);
        productOptionRepository.save(productOption);
    }
}
