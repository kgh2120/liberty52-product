package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.CustomProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomProductRepository extends JpaRepository<CustomProduct, String> {

}
