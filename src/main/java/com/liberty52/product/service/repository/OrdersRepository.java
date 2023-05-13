package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.OrderStatus;
import com.liberty52.product.service.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface OrdersRepository extends JpaRepository<Orders, String> {

    Long countAllByOrderStatusAndOrderDateLessThan(OrderStatus status, LocalDate orderDate);

    void deleteAllByOrderStatusAndOrderDateLessThan(OrderStatus status, LocalDate orderDate);

}
