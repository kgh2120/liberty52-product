package com.liberty52.product.service.controller.dto;

import static com.liberty52.product.global.contants.RepresentImageUrl.LIBERTY52_FRAME_REPRESENTATIVE_URL;

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
            long totalProductPrice, int deliveryFee,
            List<OrderRetrieveProductResponse> products) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.receiverName = receiverName;
        this.receiverEmail = receiverEmail;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.productRepresentUrl = LIBERTY52_FRAME_REPRESENTATIVE_URL;
        this.products = products;
        this.deliveryFee = deliveryFee;
        this.totalProductPrice = totalProductPrice;
        this.totalPrice = totalProductPrice + deliveryFee;
    }





}
