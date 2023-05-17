package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.ProductDetailResponseDto;
import com.liberty52.product.service.controller.dto.ProductListResponseDto;
import org.springframework.data.domain.Pageable;

public interface ProductInfoRetrieveService {
    ProductListResponseDto retrieveProductList(Pageable pageable);

    ProductDetailResponseDto retrieveProductDetail(String productId);
}
