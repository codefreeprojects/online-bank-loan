package com.ol.bankloan.controllers;

import com.ol.bankloan.dao.UserDAO;
import com.ol.bankloan.dto.UpdateUserRequestDTO;
import com.ol.bankloan.enums.UserRoleEnum;
import com.ol.bankloan.models.User;
import com.ol.bankloan.dto.BasicResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserDAO userDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<BasicResponseDTO<?>> deleteUser( @PathVariable(value = "userId") Long userId){
        userDAO.deleteById(userId);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "User deleted", null), HttpStatus.OK);
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/update/{userId}")
    public ResponseEntity<BasicResponseDTO<User>> updateUser(@PathVariable(value = "userId") Long userId, @RequestBody UpdateUserRequestDTO r){
        Optional<User> _user = userDAO.findById(userId);
        if(_user.isEmpty()){
            return new ResponseEntity<>(new BasicResponseDTO<>(true, "User not found", null), HttpStatus.OK);
        }
        User user = _user.get();
        user.setFirstName(r.getFirstName());
        user.setLastName(r.getLastName());
        user.setEmail(r.getEmail());
        user.setRole(r.getRole());
        user.setPassword(passwordEncoder.encode(r.getPassword()));
        userDAO.save(user);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Record updated", user), HttpStatus.OK);
    }
}
