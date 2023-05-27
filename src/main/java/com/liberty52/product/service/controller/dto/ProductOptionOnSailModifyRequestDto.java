package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionOnSailModifyRequestDto {

    @NotNull
    Boolean onSale;

    public static ProductOptionOnSailModifyRequestDto create(boolean onSale){
        return new ProductOptionOnSailModifyRequestDto(onSale);
    }
}
