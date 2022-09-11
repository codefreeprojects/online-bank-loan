package com.ol.bankloan.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor
public class EditProfileReqDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String securityQue;
    private String securityAns;
    private String password;
    private String contactNumber;
}
