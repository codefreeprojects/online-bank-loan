package com.ol.bankloan.dao;

import com.ol.bankloan.models.Employee;
import com.ol.bankloan.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeDAO extends JpaRepository<Employee, Long> {
    void deleteEmployeeByUser(User user);
}
