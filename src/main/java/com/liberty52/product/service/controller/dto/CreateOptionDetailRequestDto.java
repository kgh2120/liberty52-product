package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateOptionDetailRequestDto {

    @NotBlank
    String name;

    @NotNull
    Integer price;

    @NotNull
    Boolean onSale;

    public static CreateOptionDetailRequestDto create(String name, int price, boolean onSale){
        CreateOptionDetailRequestDto dto = new CreateOptionDetailRequestDto();
        dto.name = name;
        dto.price = price;
        dto.onSale = onSale;
        return dto;
    }


}
