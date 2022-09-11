package com.ol.bankloan.dao;

import com.ol.bankloan.models.Documents;
import com.ol.bankloan.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentDAO extends JpaRepository<Documents, Long> {
    List<Documents> findAllByUser(User user);
}
