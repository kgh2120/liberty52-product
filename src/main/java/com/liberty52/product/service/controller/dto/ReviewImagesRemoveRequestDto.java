package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewImagesRemoveRequestDto {
    @NotEmpty
    private List<String> urls;

    public static ReviewImagesRemoveRequestDto createForTest(List<String> urls) {
        ReviewImagesRemoveRequestDto dto = new ReviewImagesRemoveRequestDto();
        dto.urls = urls;
        return dto;
    }
}
