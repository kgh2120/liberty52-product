package com.liberty52.product.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CanceledOrders {

    @Id
    private String id = UUID.randomUUID().toString();
    private String reason;
    private LocalDateTime reqAt = LocalDateTime.now();
    private LocalDateTime canceledAt;
    private int fee;
    /** 승인자 이름. */
    private String approvedAdminName;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    private CanceledOrders(String reason) {
        this.reason = reason;
    }

    public static CanceledOrders of(String reason, Orders order) {
        CanceledOrders canceledOrders = new CanceledOrders(reason);
        canceledOrders.associate(order);
        return canceledOrders;
    }

    public void associate(Orders order) {
        this.orders = order;
        this.orders.setCanceledOrders(this);
    }

    public void approveCanceled(int fee, String approvedAdminName) {
        this.canceledAt = LocalDateTime.now();
        this.fee = fee;
        this.approvedAdminName = approvedAdminName;
    }

}
