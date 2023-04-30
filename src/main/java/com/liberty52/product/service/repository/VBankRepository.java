package com.liberty52.product.service.repository;

import com.liberty52.product.service.entity.payment.VBank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VBankRepository extends JpaRepository<VBank, String> {

    boolean existsByAccount(String account);

}
