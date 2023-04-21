package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, String> {
}
