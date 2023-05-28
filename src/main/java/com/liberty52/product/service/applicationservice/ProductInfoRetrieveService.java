package com.liberty52.product.service.applicationservice;

import com.liberty52.product.service.controller.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductInfoRetrieveService {
    ProductListResponseDto retrieveProductList(Pageable pageable);

    ProductDetailResponseDto retrieveProductDetail(String productId);

    List<ProductOptionResponseDto> retrieveProductOptionInfoListByAdmin(String role, String productId, boolean onSale);

    List<ProductInfoRetrieveResponseDto> retrieveProductListByAdmin(String role);

    ProductInfoRetrieveResponseDto retrieveProductByAdmin(String role, String productId);

    List<ProductInfoByCartResponseDto> retrieveProductOptionListByCart(String authId);
}
