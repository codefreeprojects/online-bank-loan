package com.ol.bankloan.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ol.bankloan.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity @NoArgsConstructor @AllArgsConstructor @Data
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String securityQue;
    @JsonIgnore
    private String securityAns;
    @JsonIgnore
    private String password;
    private String contactNumber;
    private UserRoleEnum role;
    private Date createdOn;
    private Boolean active;
}
