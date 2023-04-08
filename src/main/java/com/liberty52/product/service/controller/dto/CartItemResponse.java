package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CartItemResponse {

    String productId;

    String productName;

    long productPrice;

    int ea;

    String image;

    List<CartOptionResponse> optionRequestList;

    public static CartItemResponse of(String productId, String productName, long price, int ea, String image, List<CartOptionResponse> optionRequestList){
        return new CartItemResponse(productId, productName, price, ea, image, optionRequestList);
    }
}
