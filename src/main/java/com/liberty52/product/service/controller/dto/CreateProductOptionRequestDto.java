package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductOptionRequestDto {

    @NotBlank
    String name;

    @NotNull
    Boolean require;

    @NotNull
    Boolean onSail;

    public static CreateProductOptionRequestDto create(String name, boolean require, boolean onSail){
        return new CreateProductOptionRequestDto(name, require, onSail);
    }
}
