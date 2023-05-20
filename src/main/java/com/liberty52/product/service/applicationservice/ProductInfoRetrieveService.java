package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.ProductDetailResponseDto;
import com.liberty52.product.service.controller.dto.ProductInfoRetrieveResponseDto;
import com.liberty52.product.service.controller.dto.ProductListResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductInfoRetrieveService {
    ProductListResponseDto retrieveProductList(Pageable pageable);

    ProductDetailResponseDto retrieveProductDetail(String productId);

    List<ProductInfoRetrieveResponseDto> retrieveProductListByAdmin(String role);

    ProductInfoRetrieveResponseDto retrieveProductByAdmin(String role, String productId);
}
