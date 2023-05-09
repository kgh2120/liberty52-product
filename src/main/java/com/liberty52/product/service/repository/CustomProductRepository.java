package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.CustomProduct;
import feign.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomProductRepository extends JpaRepository<CustomProduct, String> {
}
