package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.ProductOption;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<ProductOption, String> {
}