package com.ol.bankloan.dto;

import com.ol.bankloan.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor
public class ApplyLoanDTO {
    private Long user_id;
    private Long loanId;
    private String panNumber;
    private Double loanAmount;
    private GenderEnum gender;
    private Integer age;
    private Date dob;
    private Long monthlyIncome;
    private String occupation;
    private String education;
    private String guarntorName;
}
