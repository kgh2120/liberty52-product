package com.liberty52.product.service.controller.dto;

import com.liberty52.product.service.entity.CustomProductOption;
import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.payment.Payment;
import com.liberty52.product.service.entity.payment.Payment.PaymentInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.liberty52.product.global.constants.RepresentImageUrl.LIBERTY52_FRAME_REPRESENTATIVE_URL;

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
    private String orderNum;
    private String paymentType;
    private PaymentInfo paymentInfo;
    private List<OrderRetrieveProductResponse> products;

    public OrdersRetrieveResponse(Orders orders) {
        this.orderId = orders.getId();
        this.orderDate = orders.getOrderDate().toString();
        this.orderStatus = orders.getOrderStatus().name();
        this.address = orders.getOrderDestination().getAddress1() + " " + orders.getOrderDestination().getAddress2();
        this.receiverName = orders.getOrderDestination().getReceiverName();
        this.receiverEmail = orders.getOrderDestination().getReceiverEmail();
        this.receiverPhoneNumber = orders.getOrderDestination().getReceiverPhoneNumber();
        this.productRepresentUrl = LIBERTY52_FRAME_REPRESENTATIVE_URL;
        this.orderNum = orders.getOrderNum();
        Payment payment = orders.getPayment();
        this.paymentType = payment.getType().getKorName();
        this.paymentInfo = payment.getInfoAsDto();
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
    }
    public OrdersRetrieveResponse(String orderId, String orderDate, String orderStatus,
            String address,
            String receiverName, String receiverEmail, String receiverPhoneNumber,
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
    }
}
