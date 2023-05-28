package com.liberty52.product.service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_option")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int fee;
    private LocalDateTime feeUpdatedAt;

    private DeliveryOption(int fee) {
        this.fee = fee;
        this.feeUpdatedAt = LocalDateTime.now();
    }

    public static DeliveryOption feeOf(int fee) {
        return new DeliveryOption(fee);
    }

    public void updateFee(int fee) {
        this.fee = fee;
        this.feeUpdatedAt = LocalDateTime.now();
    }
}
