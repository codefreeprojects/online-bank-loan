package com.ol.bankloan.dao;

import com.ol.bankloan.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDAO  extends JpaRepository<Payment, Long> {
}
