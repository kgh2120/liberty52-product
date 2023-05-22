package com.liberty52.product.service.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionDetailResponseDto {

    String optionDetailId;
    String optionDetailName;
    int price;
    boolean onSail;

    public static ProductOptionDetailResponseDto of(String optionDetailId, String optionDetailName, int price, boolean onSale) {
        return new ProductOptionDetailResponseDto(optionDetailId, optionDetailName, price, onSale);
    }
}
