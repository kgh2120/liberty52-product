package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OptionDetailRemoveRequestDto {

    @NotNull
    Boolean onSail;

    public static OptionDetailRemoveRequestDto create(boolean onSail){
        OptionDetailRemoveRequestDto dto = new OptionDetailRemoveRequestDto();
        dto.onSail = onSail;
        return dto;
    }
}
