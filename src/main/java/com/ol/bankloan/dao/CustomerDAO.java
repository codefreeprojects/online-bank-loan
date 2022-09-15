package com.ol.bankloan.dao;

import com.ol.bankloan.models.Customer;
import com.ol.bankloan.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerDAO extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUser(User user);
}
