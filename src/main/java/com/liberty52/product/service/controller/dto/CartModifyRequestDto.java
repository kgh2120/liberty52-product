package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class CartModifyRequestDto {

  private List<String> options;

  @Min(1)
  private int quantity;

  public static CartModifyRequestDto create(List<String> options,int quantity){
    return new CartModifyRequestDto(options,quantity);
  }
}
