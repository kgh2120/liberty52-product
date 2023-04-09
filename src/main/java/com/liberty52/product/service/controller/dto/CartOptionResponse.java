package com.liberty52.product.service.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CartOptionResponse {
    String optionId;
    String optionName;
    String detailOptionId;
    String detailOptionName;
    int price;

    public static CartOptionResponse of(String optionId, String optionName, String detailOptionId, String detailOptionName, int price){
    return new CartOptionResponse(optionId, optionName, detailOptionId, detailOptionName, price);
    }
}
