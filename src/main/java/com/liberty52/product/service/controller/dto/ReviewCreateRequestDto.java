package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewCreateRequestDto {
  @NotBlank
  private String productName;
  @NotNull
  @Min(1)
  @Max(5)
  private Integer rating;
  @NotEmpty
  @Size(min = 1, max = 1000)
  private String content;

  public static ReviewCreateRequestDto createForTest(String productName,Integer rating, String content) {
    ReviewCreateRequestDto dto = new ReviewCreateRequestDto();
    dto.productName = productName;
    dto.rating = rating;
    dto.content = content;
    return dto;
  }
}
