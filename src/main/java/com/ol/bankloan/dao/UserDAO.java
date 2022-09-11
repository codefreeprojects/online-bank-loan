package com.ol.bankloan.dao;

import com.ol.bankloan.enums.UserRoleEnum;
import com.ol.bankloan.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findAllByRole(UserRoleEnum role);

}
