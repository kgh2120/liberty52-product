package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, String> {
    List<CartItem> findByAuthId(String authId);
}