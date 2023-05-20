package com.liberty52.product.service.controller.dto;

import com.liberty52.product.service.entity.ProductState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ProductInfoRetrieveResponseDto {

    String id;
    String pictureUrl;
    String name;
    long price;
    float meanRating;
    int ratingCount;
    private ProductState state;

    public static ProductInfoRetrieveResponseDto of(String id, String pictureUrl, String name, long price, float meanRating, int ratingCount, ProductState state){
        return new ProductInfoRetrieveResponseDto(id, pictureUrl, name, price, meanRating, ratingCount, state);
    }

}
