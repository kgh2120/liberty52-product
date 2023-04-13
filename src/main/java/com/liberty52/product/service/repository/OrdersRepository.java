package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,String> {

}
