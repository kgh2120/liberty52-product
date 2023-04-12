package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.CustomProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
    기존에 Cart의 역할을 하고 있는 듯 해서 이걸 수정해서 Cart를 대체하도록 하는 것이 좋아보임.
 */
public interface CartItemRepository extends JpaRepository<CustomProduct, String> {
    List<CustomProduct> findByAuthId(String authId);
}