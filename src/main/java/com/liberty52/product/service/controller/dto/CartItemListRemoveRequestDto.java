package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class CartItemListRemoveRequestDto {
    @NotEmpty
    private List<String> ids;

    public static CartItemListRemoveRequestDto createForTest(List<String> ids) {
        CartItemListRemoveRequestDto dto = new CartItemListRemoveRequestDto();
        dto.ids = ids;
        return dto;
    }
}
