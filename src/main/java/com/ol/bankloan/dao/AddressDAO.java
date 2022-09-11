package com.ol.bankloan.dao;

import com.ol.bankloan.models.Address;
import com.ol.bankloan.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDAO extends JpaRepository<Address, Long> {
    Boolean existsByUser(User user);
}
