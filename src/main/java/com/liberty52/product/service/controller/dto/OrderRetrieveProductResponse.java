package com.liberty52.product.service.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class OrderRetrieveProductResponse {

    private String customProductId;
    private String name;
    private int quantity;
    private Long price;

    private String productUrl;

    private List<String> options;
    private boolean hasReview;


    @QueryProjection
    public OrderRetrieveProductResponse(String name, int quantity, Long price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    @QueryProjection
    public OrderRetrieveProductResponse(String customProductId, String name, int quantity, Long price, String productUrl, boolean hasReview, List<String> options) {
        this.customProductId = customProductId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.productUrl = productUrl;
        this.options = options;
        this.hasReview = hasReview;
    }
}
