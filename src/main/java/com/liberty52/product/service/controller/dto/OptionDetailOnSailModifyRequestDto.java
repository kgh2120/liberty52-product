package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OptionDetailOnSailModifyRequestDto {

    @NotNull
    Boolean onSale;

    public static OptionDetailOnSailModifyRequestDto create(boolean onSale){
        OptionDetailOnSailModifyRequestDto dto = new OptionDetailOnSailModifyRequestDto();
        dto.onSale = onSale;
        return dto;
    }
}
