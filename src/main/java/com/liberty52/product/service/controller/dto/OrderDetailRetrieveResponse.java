package com.liberty52.product.service.controller.dto;

import static com.liberty52.product.global.contants.RepresentImageUrl.LIBERTY52_FRAME_REPRESENTATIVE_URL;

import com.liberty52.product.service.entity.OrderDestination;
import com.liberty52.product.service.entity.Orders;
import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
    public OrderDetailRetrieveResponse(Orders orders) {
        this.orderId = orders.getId();
        this.orderDate = orders.getOrderDate().toString();
        this.orderStatus = orders.getOrderStatus().name();
        OrderDestination destination = orders.getOrderDestination();
        this.address = destination.getAddress1()+" " + destination.getAddress2();
        this.receiverName = destination.getReceiverName();
        this.receiverEmail = destination.getReceiverEmail();
        this.receiverPhoneNumber = destination.getReceiverPhoneNumber();
        this.productRepresentUrl = LIBERTY52_FRAME_REPRESENTATIVE_URL;
        this.products = orders.getCustomProducts().stream().map(c ->
            new OrderRetrieveProductResponse(c.getProduct().getName(), c.getQuantity(),
                    c.getProduct().getPrice() + c.getOptions()
                            .stream()
                            .mapToLong(e
                                    -> e.getOptionDetail()
                                    .getPrice())
                            .sum(),
                    c.getUserCustomPictureUrl(),
                    c.getOptions().stream().map(o ->
                            o.getOptionDetail().getName()).toList())
        ).toList();
        this.deliveryFee = orders.getDeliveryPrice();
        this.products.forEach(p ->
                    this.totalProductPrice += p.getPrice());
        this.totalPrice = totalProductPrice + deliveryFee;
    }

    public OrderDetailRetrieveResponse(String orderId, String orderDate, String orderStatus,
            String address,
            String receiverName, String receiverEmail, String receiverPhoneNumber,
            int deliveryFee,
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
        this.products.forEach(p ->
                this.totalProductPrice += p.getPrice());
        this.totalPrice = totalProductPrice + deliveryFee;
    }




}
