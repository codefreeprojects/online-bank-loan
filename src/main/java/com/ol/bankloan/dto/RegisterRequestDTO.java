package com.ol.bankloan.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ol.bankloan.enums.UserRoleEnum;
import com.ol.bankloan.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String securityAns;
    private String securityQue;
    private String contactNumber;

    private String  password;

    private String  confirmPassword;

    private UserRoleEnum role;

    @JsonIgnore
    public User getUserByThisDTO(){
        User user = new User();
        user.setFirstName(this.firstName);
        user.setMiddleName(this.middleName);
        user.setLastName(this.lastName);
        user.setEmail(this.email);
        user.setSecurityQue(this.securityQue);
        user.setSecurityAns(this.securityAns);
        user.setContactNumber(this.contactNumber);
        user.setRole(this.role);
        user.setActive(true);
        user.setCreatedOn(new Date());
        return user;
    }
}
