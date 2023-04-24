package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment<?>, String> {
}
