package com.ol.bankloan.models;

import com.ol.bankloan.enums.GenderEnum;
import com.ol.bankloan.enums.LoanStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.DETACH)
    private User user;
    private String panNumber;
    @OneToOne(cascade = CascadeType.DETACH)
    private Loan loan;
    private Double loanAmount;
    private GenderEnum gender;
    private Integer age;
    @Temporal(TemporalType.DATE)
    private Date dob;
    private Long monthlyIncome;
    private String occupation;
    private String education;
    private String guarntorName;
    private LoanStatusEnum loanStatus;
}
