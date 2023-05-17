package com.liberty52.product.service.applicationservice;

import com.liberty52.product.global.exception.external.notfound.ResourceNotFoundException;
import com.liberty52.product.service.controller.dto.ProductDetailResponseDto;
import com.liberty52.product.service.controller.dto.ProductListResponseDto;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductInfoRetrieveServiceImpl implements ProductInfoRetrieveService {
    private final ProductRepository productRepository;

    @Override
    public ProductListResponseDto retrieveProductList(Pageable pageable) {
        return new ProductListResponseDto(productRepository.findAll(pageable));
    }

    @Override
    public ProductDetailResponseDto retrieveProductDetail(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product", "id", productId));
        return new ProductDetailResponseDto(product);
    }
}
