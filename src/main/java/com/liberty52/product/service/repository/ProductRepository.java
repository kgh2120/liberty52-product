package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

  Optional<Product> findByName(String name);
}

