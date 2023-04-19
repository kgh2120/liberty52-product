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

  private String customProductId;

  public static CartModifyRequestDto create(List<String> options,int quantity,String id){
    return new CartModifyRequestDto(options,quantity,id);
  }
}
