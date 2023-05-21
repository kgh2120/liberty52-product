package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VBankStatusModifyDto {
  @NotBlank
  private String depositorBank;
  @NotBlank
  private String depositorName;
  @NotBlank
  private String depositorAccount;

}
