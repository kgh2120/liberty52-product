package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class CartItemRequest {

    @NotBlank
    String productId;

    @NotBlank
    int ea;

    @NotBlank
    List<OptionRequest> optionRequestList;
}
