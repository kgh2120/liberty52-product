package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {
}