package com.liberty52.product.service.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class MonoItemOrderRequestDto {
    @NotBlank
    private String productName;
    private List<String> options;
    @Min(1)
    private int quantity;
    @Min(0)
    private int deliveryPrice;

    public static MonoItemOrderRequestDto createForTest(String productName, List<String> details, int quantity, int deliveryPrice) {
        return builder().productName(productName).options(details).quantity(quantity).deliveryPrice(deliveryPrice).build();
    }
}
