package com.liberty52.product.service.controller.dto;

import com.liberty52.product.service.entity.OptionDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionDetailResponseDto {

    String optionDetailId;
    String optionDetailName;
    int price;
    boolean onSale;

    public ProductOptionDetailResponseDto(OptionDetail optionDetail) {
         optionDetailId = optionDetail.getId();
         optionDetailName = optionDetail.getName();
         price = optionDetail.getPrice();
         onSale = optionDetail.isOnSale();
    }

}
