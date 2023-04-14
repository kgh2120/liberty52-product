package com.liberty52.product.service.entity;

import com.liberty52.product.global.exception.external.AlreadyCompletedOrderException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Orders {

    @Id
    private String id = UUID.randomUUID().toString();

    @Column(unique = true, updatable = false)
    private String authId;

    private LocalDate orderDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    private int deliveryPrice;

    @OneToMany(mappedBy = "orders")
    private List<CustomProduct> customProducts = new ArrayList<>();

    @JoinColumn(name = "order_destination_id")
    @OneToOne(cascade = CascadeType.ALL)
    private OrderDestination orderDestination;

    private Orders(String authId, int deliveryPrice, OrderDestination orderDestination) {
        this.authId = authId;
        orderStatus = OrderStatus.ORDERED;
        this.deliveryPrice = deliveryPrice;
        this.orderDestination = orderDestination;
    }

    // 따로 addCustomProduct 를 만들지 않은 이유는
    // Orders는 이미 결제 완료된 상태이기 때문에 제품이 변하지 않을 것이라고 생각.
    public static Orders create(String authId, int deliveryPrice, OrderDestination orderDestination){

        return new Orders(authId,deliveryPrice,orderDestination);
    }

    public void associateWithCustomProduct(List<CustomProduct> customProducts){
        customProducts.forEach(cp ->
                cp.associateWithOrder(this));
        this.customProducts = customProducts;
    }
    public void changeOrderStatusToNextStep(){
        if(orderStatus.equals(OrderStatus.COMPLETE))
            throw new AlreadyCompletedOrderException();
        this.orderStatus = OrderStatus.values()[orderStatus.ordinal()+1];
    }
}
