package com.liberty52.product.service.entity;

import com.liberty52.product.global.contants.PriceConstants;
import com.liberty52.product.global.util.Utils;
import com.liberty52.product.service.entity.payment.Payment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Slf4j
public class Orders {

    @Id
    private final String id = UUID.randomUUID().toString();

    @Column(updatable = false, nullable = false)
    private String authId;

    private final LocalDate orderDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int deliveryPrice = PriceConstants.DEFAULT_DELIVERY_PRICE;

    private Long amount;

    private Integer totalQuantity;

    private String orderNum;

    @OneToMany(mappedBy = "orders")
    private List<CustomProduct> customProducts = new ArrayList<>();

    @JoinColumn(name = "order_destination_id")
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private OrderDestination orderDestination;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "orders")
    @JoinColumn(updatable = false)
    private Payment payment;

    @Deprecated
    private Orders(String authId, int deliveryPrice, OrderDestination orderDestination) {
        this.authId = authId;
        orderStatus = OrderStatus.ORDERED;
        this.orderNum = Utils.OrderNumberBuilder.createOrderNum();
        this.deliveryPrice = deliveryPrice;
        this.orderDestination = orderDestination;
    }

    private Orders(String authId, OrderDestination orderDestination) {
        this.authId = authId;
        this.orderStatus = OrderStatus.READY;
        this.orderNum = Utils.OrderNumberBuilder.createOrderNum();
        this.orderDestination = orderDestination;
    }

    @Deprecated
    public static Orders create(String authId, int deliveryPrice, OrderDestination orderDestination){
        return new Orders(authId,deliveryPrice,orderDestination);
    }

    public static Orders create(String authId, OrderDestination orderDestination){
        return new Orders(authId, orderDestination);
    }

    void addCustomProduct(CustomProduct customProduct) {
        this.customProducts.add(customProduct);
    }

    public void setPayment(Payment<?> payment) {
        this.payment = payment;
    }

    public void changeOrderStatusToOrdered() {
        this.orderStatus = OrderStatus.ORDERED;
    }

    public void changeOrderStatusToWaitingDeposit() {
        this.orderStatus = OrderStatus.WAITING_DEPOSIT;
    }

    public void calculateTotalValueAndSet() {
        this.calcTotalAmountAndSet();
        this.calcTotalQuantityAndSet();
    }

    private void calcTotalAmountAndSet() {
        AtomicLong totalAmount = new AtomicLong();

        this.customProducts.forEach(customProduct -> {
            // 기본금
            totalAmount.getAndAdd(customProduct.getProduct().getPrice());
            // 옵션 추가금액
            customProduct.getOptions().forEach(customProductOption ->
                        totalAmount.getAndAdd(customProductOption.getOptionDetail().getPrice()));
            // 수량
            totalAmount.getAndUpdate(x -> customProduct.getQuantity() * x);
        });
        // 배송비
        totalAmount.getAndAdd(this.deliveryPrice);

        this.amount = totalAmount.get();
    }

    private void calcTotalQuantityAndSet() {
        AtomicInteger quantity = new AtomicInteger();

        this.customProducts.forEach(customProduct -> quantity.getAndAdd(customProduct.getQuantity()));

        this.totalQuantity = quantity.get();
    }


}
