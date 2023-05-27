package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.ProductOptionModifyRequestDto;

public interface ProductOptionModifyService {
    void modifyProductOptionByAdmin(String role, String productOptionId, ProductOptionModifyRequestDto dto);

    void modifyProductOptionOnSailStateByAdmin(String role, String productOptionId);

}
