package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.ProductInfoRetrieveService;
import com.liberty52.product.service.controller.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductInfoRetrieveController {
    private final ProductInfoRetrieveService productInfoRetrieveService;

    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    public ProductListResponseDto retrieveProductList(Pageable pageable) {
        return productInfoRetrieveService.retrieveProductList(pageable);
    }

    @GetMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDetailResponseDto retrieveProductDetail(@PathVariable String productId) {
        return productInfoRetrieveService.retrieveProductDetail(productId);
    }

    @GetMapping("/admin/productOptionInfo/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductOptionResponseDto> retrieveProductOptionInfoListByAdmin(@RequestHeader("LB-Role") String role, @PathVariable String productId, @RequestParam boolean onSale) {
        return productInfoRetrieveService.retrieveProductOptionInfoListByAdmin(role, productId, onSale);
    }

    @GetMapping("/admin/productInfo")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductInfoRetrieveResponseDto> retrieveProductListByAdmin(@RequestHeader("LB-Role") String role) {
        return productInfoRetrieveService.retrieveProductListByAdmin(role);
    }

    @GetMapping("/admin/productInfo/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductInfoRetrieveResponseDto retrieveProductByAdmin(@RequestHeader("LB-Role") String role, @PathVariable String productId) {
        return productInfoRetrieveService.retrieveProductByAdmin(role, productId);
    }

    @GetMapping("/productOptionInfoByCart")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductInfoByCartResponseDto> retrieveProductOptionListByCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String authId) {
        return productInfoRetrieveService.retrieveProductOptionListByCart(authId);
    }
}
