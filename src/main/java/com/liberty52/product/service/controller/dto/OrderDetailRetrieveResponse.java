package com.liberty52.product.service.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Data;

@Data
public class OrderDetailRetrieveResponse {

    private String orderId;
    private String orderDate;
    private String orderStatus;
    private String address;
    private String receiverEmail;
    private String receiverName;
    private String receiverPhoneNumber;
    private String productRepresentUrl;
    private long totalProductPrice;
    private int deliveryFee;
    private long totalPrice;

    private List<OrderRetrieveProductResponse> products;

    @QueryProjection
    public OrderDetailRetrieveResponse(String orderId, String orderDate, String orderStatus,
            String address,
            String receiverName, String receiverEmail, String receiverPhoneNumber,
            String productRepresentUrl,
            long totalProductPrice, int deliveryFee,
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
        this.deliveryFee = deliveryFee;
        this.totalProductPrice = totalProductPrice;
        this.totalPrice = totalProductPrice + deliveryFee;
    }





}
