package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, String> {

    List<Orders> findAllByOrderStatusAndOrderedAtLessThan(OrderStatus status, LocalDateTime orderedAt);

}
