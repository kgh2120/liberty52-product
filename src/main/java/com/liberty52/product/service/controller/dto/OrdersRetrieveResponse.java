package com.liberty52.product.service.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrdersRetrieveResponse {

    private String orderId;
    private String orderDate;
    private String orderStatus;
    private String address;
    private String receiverName;
    private String receiverEmail;
    private String receiverPhoneNumber;
    private String productRepresentUrl;
    private List<OrderRetrieveProductResponse> products;

    @QueryProjection
    public OrdersRetrieveResponse(String orderId, String orderDate, String orderStatus,
            String address,
            String receiverName, String receiverEmail, String receiverPhoneNumber,
            String productRepresentUrl,
            List<OrderRetrieveProductResponse> products) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.receiverName = receiverName;
        this.receiverEmail = receiverEmail;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.productRepresentUrl = productRepresentUrl;
        this.products = products;
    }
}
