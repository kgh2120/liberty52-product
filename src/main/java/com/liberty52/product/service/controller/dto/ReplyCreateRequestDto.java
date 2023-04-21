package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReplyCreateRequestDto {
  @NotEmpty
  @Size(min = 1, max = 1000)
  private String content;
  public static ReplyCreateRequestDto createForTest(String content) {
    ReplyCreateRequestDto dto = new ReplyCreateRequestDto();
    dto.content = content;
    return dto;
  }
}
