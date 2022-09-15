package com.ol.bankloan.dao;

import com.ol.bankloan.enums.LoanTypeEnum;
import com.ol.bankloan.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanDAO extends JpaRepository<Loan, Long> {
    Optional<Loan> findByLoanType(LoanTypeEnum loanType);
}
