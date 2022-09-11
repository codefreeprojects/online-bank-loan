package com.ol.bankloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddAddressRequestDTO {
    private String houseNumber;
    private String street;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private String pinCode;
}
