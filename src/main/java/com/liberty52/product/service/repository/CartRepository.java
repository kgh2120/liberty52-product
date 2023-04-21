package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByAuthId(String authId);

    Optional<Cart> findByAuthIdAndExpiryDateGreaterThanEqual(String guestId, LocalDate expiryDate);

}