package com.liberty52.product.service.controller;

import com.liberty52.product.service.applicationservice.OptionDetailCreateService;
import com.liberty52.product.service.applicationservice.ProductOptionCreateService;
import com.liberty52.product.service.controller.dto.CreateOptionDetailRequestDto;
import com.liberty52.product.service.controller.dto.CreateProductOptionRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductOptionCreateController {

    private final ProductOptionCreateService productOptionCreateService;

    @PostMapping("/admin/productOption/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProductOptionByAdmin(@RequestHeader("LB-Role") String role,
                                          @Validated @RequestBody CreateProductOptionRequestDto dto, @PathVariable String productId) {
        productOptionCreateService.createProductOptionByAdmin(role, dto, productId);
    }
}
