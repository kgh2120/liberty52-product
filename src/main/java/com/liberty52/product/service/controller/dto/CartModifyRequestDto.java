package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.Getter;

@Getter
public class CartModifyRequestDto {

  private List<String> options;

  @Min(1)
  private int quantity;
}
