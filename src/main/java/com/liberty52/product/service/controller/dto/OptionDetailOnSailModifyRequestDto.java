package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OptionDetailOnSailModifyRequestDto {

    @NotNull
    Boolean onSail;

    public static OptionDetailOnSailModifyRequestDto create(boolean onSail){
        OptionDetailOnSailModifyRequestDto dto = new OptionDetailOnSailModifyRequestDto();
        dto.onSail = onSail;
        return dto;
    }
}
