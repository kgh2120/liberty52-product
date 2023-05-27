package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.CreateProductOptionRequestDto;

public interface ProductOptionCreateService {
    void createProductOptionByAdmin(String role, CreateProductOptionRequestDto dto, String productId);
}
