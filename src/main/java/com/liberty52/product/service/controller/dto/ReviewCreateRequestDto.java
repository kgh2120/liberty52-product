package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewCreateRequestDto {
  @NotNull
  @Min(1)
  @Max(5)
  private Integer rating;
  @NotEmpty
  @Size(min = 1, max = 1000)
  private String content;
  @NotEmpty
  String customProductId;

  public static ReviewCreateRequestDto createForTest(Integer rating, String content,String customProductId) {
    ReviewCreateRequestDto dto = new ReviewCreateRequestDto();
    dto.rating = rating;
    dto.content = content;
    dto.customProductId = customProductId;
    return dto;
  }
}
