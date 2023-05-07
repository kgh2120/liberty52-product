package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReplyModifyRequestDto {
  @NotEmpty
  @Size(min = 1, max = 1000)
  private String content;
  public static ReplyModifyRequestDto createForTest(String content) {
    ReplyModifyRequestDto dto = new ReplyModifyRequestDto();
    dto.content = content;
    return dto;
  }
}
