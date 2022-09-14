package com.ol.bankloan.dao;

import com.ol.bankloan.models.Customer;
import com.ol.bankloan.models.EMI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmiDAO extends JpaRepository<EMI, Long> {
    List<EMI> findAllByCustomer(Customer customer);
    void deleteByCustomer(Customer customer);
}
