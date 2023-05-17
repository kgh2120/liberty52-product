package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.ProductInfoRetrieveService;
import com.liberty52.product.service.controller.dto.ProductDetailResponseDto;
import com.liberty52.product.service.controller.dto.ProductListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductInfoRetrieveController {
    private final ProductInfoRetrieveService productInfoRetrieveService;

    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    public ProductListResponseDto productList(Pageable pageable) {
        return productInfoRetrieveService.retrieveProductList(pageable);
    }

    @GetMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDetailResponseDto productDetail(@PathVariable String productId) {
        return productInfoRetrieveService.retrieveProductDetail(productId);
    }
}
