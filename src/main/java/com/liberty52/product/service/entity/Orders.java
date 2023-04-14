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
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private OrderDestination orderDestination;

    private Orders(String authId, int deliveryPrice, OrderDestination orderDestination) {
        this.authId = authId;
        orderStatus = OrderStatus.ORDERED;
        this.deliveryPrice = deliveryPrice;
        this.orderDestination = orderDestination;
    }

    public static Orders create(String authId, int deliveryPrice, OrderDestination orderDestination){
        return new Orders(authId,deliveryPrice,orderDestination);
    }

    public void changeOrderStatusToNextStep(){
        if(orderStatus.equals(OrderStatus.COMPLETE))
            throw new AlreadyCompletedOrderException();
        this.orderStatus = OrderStatus.values()[orderStatus.ordinal()+1];
    }
    public void associateWithCustomProduct(List<CustomProduct> customProducts){
        customProducts.forEach(cp ->
                cp.associateWithOrder(this));
        this.customProducts = customProducts;
    }


    void addCustomProduct(CustomProduct customProduct) {
        this.customProducts.add(customProduct);
    }

}
