package com.ol.bankloan.controllers;

import com.ol.bankloan.dao.*;
import com.ol.bankloan.dto.ApplyLoanDTO;
import com.ol.bankloan.dto.BasicResponseDTO;
import com.ol.bankloan.dto.EditProfileReqDTO;
import com.ol.bankloan.enums.LoanStatusEnum;
import com.ol.bankloan.models.Customer;
import com.ol.bankloan.models.EMI;
import com.ol.bankloan.models.Loan;
import com.ol.bankloan.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    LoanDAO loanDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    EmiDAO emiDAO;

    @Autowired
    CustomerDAO customerDAO;
    @Autowired
    AddressDAO addressDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ModelMapper mapper = new ModelMapper();

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/edit-profile/{userId}")
    public ResponseEntity<BasicResponseDTO<User>> editProfile(@PathVariable("userId") Long userId, @RequestBody EditProfileReqDTO editProfileReqDTO){
        User user = this.mapper.map(editProfileReqDTO, User.class);
        Optional<User> tUser = userDAO.findById(userId);
        if(tUser.isPresent()){
            User _user = tUser.get();
            user.setRole(_user.getRole());
            user.setCreatedOn(_user.getCreatedOn());
            user.setActive(_user.getActive());
            user.setId(userId);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDAO.save(user);
        }
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Updated Information", user), HttpStatus.OK);
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/view-types-of-loans")
    public ResponseEntity<BasicResponseDTO<List<Loan>>> getAllLoanTypes(){
        List<Loan> loans = loanDAO.findAll();
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "", loans), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/emi-details/{customerId}")
    public ResponseEntity<BasicResponseDTO<List<EMI>>> getALlEmilDetails(@PathVariable("customerId") Long customerId){
        Optional<Customer> customer = customerDAO.findById(customerId);
        if(customer.isPresent()){
            List<EMI> emis = emiDAO.findAllByCustomer(customer.get());
            return new ResponseEntity<>(new BasicResponseDTO<>(true, "Record found", emis), HttpStatus.OK);
        }
        return new ResponseEntity<>(new BasicResponseDTO<>(false, "No record found", null), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/apply-for-loan/{loanId}")
    public ResponseEntity<BasicResponseDTO<Customer>> applyForLoan(@PathVariable("loanId") Long loanId, @RequestBody ApplyLoanDTO applyLoanDTO){
        Customer customer = this.mapper.map(applyLoanDTO, Customer.class);
        Optional<Loan> _loan = loanDAO.findById(loanId);
        Optional<User> _user = userDAO.findById(applyLoanDTO.getUser_id());
        if(_user.isPresent() && _loan.isPresent()){
            if(!addressDAO.existsByUser(_user.get())){
                return new ResponseEntity<>(new BasicResponseDTO<>(false, "Please add address details first before apply for loan", null), HttpStatus.CONFLICT);
            }
            customer.setLoanStatus(LoanStatusEnum.PENDING);
            customer.setUser(_user.get());
            customer.setLoan(_loan.get());
            customerDAO.save(customer);
            return new ResponseEntity<>(new BasicResponseDTO<>(true, "Record found", customer), HttpStatus.OK);
        }
        return new ResponseEntity<>(new BasicResponseDTO<>(false, "User or loan not found", null), HttpStatus.OK);
    }

}
