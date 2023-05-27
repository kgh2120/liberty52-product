package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionModifyRequestDto {

    @NotBlank
    String name;

    @NotNull
    Boolean require;

    @NotNull
    Boolean onSale;

    public static ProductOptionModifyRequestDto create(String name, boolean require, boolean onSale){
        return new ProductOptionModifyRequestDto(name, require, onSale);
    }
}
