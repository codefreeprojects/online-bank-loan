package com.ol.bankloan.controllers;

import com.ol.bankloan.dao.*;
import com.ol.bankloan.dto.*;
import com.ol.bankloan.enums.UserRoleEnum;
import com.ol.bankloan.models.*;
import com.ol.bankloan.services.UserDetailsService;
import com.ol.bankloan.utils.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/admin")
public class AdminController {

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
    PaymentDAO paymentDAO;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    EmployeeDAO employeeDAO;

    private final ModelMapper mapper = new ModelMapper();

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/add-employee")
    public ResponseEntity<BasicResponseDTO<RegisterResponseDTO>> registerEmployee(@RequestBody RegisterEmpRequestDTO registerRequestDTO) {
        BasicResponseDTO<RegisterResponseDTO> basicResponseDTO = new BasicResponseDTO<>();
        basicResponseDTO.setData(null);
        basicResponseDTO.setSuccess(false);
        if( !registerRequestDTO.getPassword().equals(registerRequestDTO.getConfirmPassword()) ){
            basicResponseDTO.setMessage("Password and confirm password not matched");
            return new ResponseEntity<>(basicResponseDTO, HttpStatus.BAD_REQUEST);
        }
        if(userDAO.existsByEmail(registerRequestDTO.getEmail())){
            basicResponseDTO.setMessage("User already exists");
            return new ResponseEntity<>(basicResponseDTO, HttpStatus.CONFLICT);
        }
        User user = registerRequestDTO.getUserByThisDTO();
        user.setRole(UserRoleEnum.EMPLOYEE);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        userDAO.save(user);
        Employee employee = new Employee();
        employee.setUser(user);
        employee.setSalary(registerRequestDTO.getSalary());
        employee.setHireDate(registerRequestDTO.getHireDate());
        employee.setDesignation(registerRequestDTO.getDesignation());
        employeeDAO.save(employee);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        basicResponseDTO.setData(new RegisterResponseDTO(jwtUtil.generateToken(userDetails), user.getEmail(), user.getFirstName()));
        basicResponseDTO.setSuccess(true);
        basicResponseDTO.setMessage("Employee created successfully");
        return new ResponseEntity<>(basicResponseDTO, HttpStatus.CREATED);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @Transactional
    @DeleteMapping("/remove-employee/{empId}")
    public ResponseEntity<BasicResponseDTO<?>> removeEmployee(@PathVariable("empId") Long empId){
        Optional<Employee> _emp = employeeDAO.findById(empId);
        if(_emp.isPresent()){
            User user = _emp.get().getUser();
            employeeDAO.deleteById(empId);
            userDAO.delete(user);
            return new ResponseEntity<>(new BasicResponseDTO<>(true, "Records deleted", null), HttpStatus.OK);
        }

        return new ResponseEntity<>(new BasicResponseDTO<>(false, "Records not found", null), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/list-employees")
    public ResponseEntity<BasicResponseDTO<List<Employee>>> listAllEmployee(){
        List<Employee> users = employeeDAO.findAll();
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "All records", users), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/add-laon-type")
    public ResponseEntity<BasicResponseDTO<Loan>> addLoanType(@RequestBody AddLoanDTO addLoanDTO){
        Loan loan = this.mapper.map(addLoanDTO, Loan.class);
        Optional<Loan> l = loanDAO.findByLoanType(addLoanDTO.getLoanType());
        if(l.isPresent()){
            loan.setId(l.get().getId());
        }
        loanDAO.save(loan);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Record saved", loan), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/update-laon-type/{loanId}")
    public ResponseEntity<BasicResponseDTO<Loan>> updateLoanType(@RequestBody AddLoanDTO addLoanDTO, @PathVariable("loanId") Long loanId){
        Loan loan = this.mapper.map(addLoanDTO, Loan.class);
        Optional<Loan> _loan = loanDAO.findById(loanId);
        if(_loan.isEmpty()){
            return new ResponseEntity<>(new BasicResponseDTO<>(false, "Loan id not found", null), HttpStatus.OK);
        }
        loan.setId(loanId);
        loanDAO.save(loan);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Record updated", loan), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/view-all-loans")
    public ResponseEntity<BasicResponseDTO<List<Customer>>> viewLoanApplications(){
        List<Customer> customers = customerDAO.findAll();
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "All records", customers), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/view-payment")
    public ResponseEntity<BasicResponseDTO<List<Payment>>> viewPaymentDetails(){
        List<Payment> payments = paymentDAO.findAll();
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "All records", payments), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/view-payment/{paymentId}")
    public ResponseEntity<BasicResponseDTO<Payment>> viewPaymentDetail(@PathVariable("paymentId") Long paymentId){
        Optional<Payment> _payment = paymentDAO.findById(paymentId);
        return new ResponseEntity<>(new BasicResponseDTO<>( _payment.isPresent(), "All records", _payment.isPresent() ? _payment.get() : null), HttpStatus.OK);
    }
}
