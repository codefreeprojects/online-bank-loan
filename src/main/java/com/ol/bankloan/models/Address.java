package com.ol.bankloan.models;

import com.ol.bankloan.enums.AddressAreaEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @AllArgsConstructor @NoArgsConstructor @Data
public class Address {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.REMOVE)
    private User user;
    private String houseNumber;
    private String street;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String pinCode;
}
