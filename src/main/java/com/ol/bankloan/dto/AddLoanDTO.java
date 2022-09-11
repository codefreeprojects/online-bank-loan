package com.ol.bankloan.dto;

import com.ol.bankloan.enums.LoanTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class AddLoanDTO {
    private Double rate;
    private Long duration;
    private LoanTypeEnum loanType;
    private Double processingFee;
}
