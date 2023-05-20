package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.Orders;
import com.liberty52.product.service.entity.Product;
import com.liberty52.product.service.entity.Review;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {
  Optional<Review> findByCustomProduct_Orders(Orders orders);

  List<Review> findByCustomProduct_Product(Product product);
}