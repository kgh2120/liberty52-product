package com.liberty52.product.service.entity;

import com.liberty52.product.global.constants.PriceConstants;
import com.liberty52.product.global.util.Utils;
import com.liberty52.product.service.entity.payment.Payment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Slf4j
public class Orders {

    @Id
    private final String id = UUID.randomUUID().toString();

    @Column(updatable = false, nullable = false)
    private String authId;

    private final LocalDateTime orderedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

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
    private Payment<?> payment;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "orders")
    private CanceledOrders canceledOrders;

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

    public void setCanceledOrders(CanceledOrders canceledOrders) {
        this.canceledOrders = canceledOrders;
    }

    public void changeOrderStatusToCancelRequest() {
        this.orderStatus = OrderStatus.CANCEL_REQUESTED;
    }

    public void changeOrderStatusToCanceled() {
        this.orderStatus = OrderStatus.CANCELED;
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

    public void modifyOrderStatus(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
    }

    /**
     * 주문 생성 이후, 모든 로직이 종료되면 호출. <br>
     * 카드 결제 -> 결제 승인 로직 이후. <br>
     * 가상계좌 결제 -> 생성 로직 이후. <br>
     * <br>
     * 장바구니 카드결제 시, 결제창 호출한 후 미결제 종료함에도 장바구니를 유지시켜야 하기 때문에, <br>
     * 모든 주문 리소스 생성 이후에 호출하여 타 엔티티간의 관계를 끊어주기 위한 메소드다.
     */
    public void finishCreation() {
        this.customProducts.forEach(e -> {
            e.dissociateCart();
            e.getOptions().forEach(CustomProductOption::setOptionDetailAndDissociate);
        });
    }

}
