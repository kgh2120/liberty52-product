package com.liberty52.product.service.controller.dto;

import static com.liberty52.product.global.contants.RepresentImageUrl.LIBERTY52_FRAME_REPRESENTATIVE_URL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liberty52.product.global.exception.external.internalservererror.InvalidFormatException;
import com.liberty52.product.service.entity.OrderDestination;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.payment.Payment;
import com.querydsl.core.annotations.QueryProjection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private String orderNum;
    private String paymentType;
    private Payment.PaymentInfo paymentInfo;


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
        this.orderNum = orders.getOrderNum();

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
        this.totalPrice = orders.getAmount();
        this.totalProductPrice = totalPrice - deliveryFee;
        Payment payment = orders.getPayment();
        this.paymentType = payment.getType().getKorName();
        this.paymentInfo = payment.getInfoAsDto();
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
