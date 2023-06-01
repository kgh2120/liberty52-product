package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface OrdersRepository extends JpaRepository<Orders, String> {

    Long countAllByOrderStatusAndOrderedAtLessThan(OrderStatus status, LocalDateTime orderedAt);

    void deleteAllByOrderStatusAndOrderedAtLessThan(OrderStatus status, LocalDateTime orderedAt);

}
