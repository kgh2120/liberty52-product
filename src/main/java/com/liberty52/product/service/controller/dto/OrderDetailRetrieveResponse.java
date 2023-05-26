package com.liberty52.product.service.controller.dto;

import com.liberty52.product.service.entity.CustomProductOption;
import com.liberty52.product.service.entity.OrderDestination;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.payment.Payment;
import lombok.Data;

import java.util.List;

import static com.liberty52.product.global.constants.RepresentImageUrl.LIBERTY52_FRAME_REPRESENTATIVE_URL;

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
    private String customerName;

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
            new OrderRetrieveProductResponse(c.getId(),c.getProduct().getName(), c.getQuantity(),
                    c.getProduct().getPrice() + c.getOptions()
                            .stream()
                            .mapToLong(CustomProductOption::getPrice)
                            .sum(),
                    c.getUserCustomPictureUrl(),
                    c.getOptions().stream().map(CustomProductOption::getDetailName).toList()
            )
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

    public static OrderDetailRetrieveResponse of(Orders entity, String customerName) {
        OrderDetailRetrieveResponse response = new OrderDetailRetrieveResponse(entity);
        response.customerName = customerName;
        return response;
    }

}
