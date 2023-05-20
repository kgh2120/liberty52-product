package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.CanceledOrders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CanceledOrdersRepository extends JpaRepository<CanceledOrders, String> {
}
