package com.ol.bankloan.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ol.bankloan.enums.UserRoleEnum;
import com.ol.bankloan.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String middleName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
    @NotBlank
    private String securityAns;
    @NotBlank
    private String securityQue;
    @NotBlank
    private String contactNumber;
    @NotBlank
    private String  password;
    @NotBlank
    private String  confirmPassword;
    @NotBlank
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
