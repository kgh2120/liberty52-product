package com.liberty52.product.service.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionResponseDto {

    String optionId;
    String optionName;
    boolean require;
    boolean onSail;
    List<ProductOptionDetailResponseDto> optionDetailList;

    public static ProductOptionResponseDto of(String optionId, String optionName, boolean require, boolean onSale, List<ProductOptionDetailResponseDto> optionDetailList) {
        return new ProductOptionResponseDto(optionId, optionName, require, onSale, optionDetailList);
    }
}
