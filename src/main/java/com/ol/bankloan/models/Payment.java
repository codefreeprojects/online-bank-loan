package com.ol.bankloan.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.DETACH)
    private Customer customer;
    private String paymentNumber;
    @Temporal(TemporalType.DATE)
    private Date reciptDate;
    private Double emiAmount;
    @Temporal(TemporalType.DATE)
    private Date emiDate;
    private Double lateCharge;
    private Double totalAmount;
}
