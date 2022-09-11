package com.ol.bankloan.dto;

import com.ol.bankloan.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class UpdateUserRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String aadharNumber;
    private UserRoleEnum role;
}
