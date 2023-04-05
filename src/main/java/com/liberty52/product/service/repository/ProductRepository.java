package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}