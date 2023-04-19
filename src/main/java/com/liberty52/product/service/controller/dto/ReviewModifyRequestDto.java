package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class ReviewModifyRequestDto {
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
    @NotEmpty
    @Size(min = 1, max = 1000)
    private String content;

    public static ReviewModifyRequestDto createForTest(Integer rating, String content) {
        ReviewModifyRequestDto dto = new ReviewModifyRequestDto();
        dto.rating = rating;
        dto.content = content;
        return dto;
    }
}
