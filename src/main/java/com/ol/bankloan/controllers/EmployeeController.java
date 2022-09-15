package com.ol.bankloan.controllers;

import com.ol.bankloan.dao.*;
import com.ol.bankloan.dto.ApplyLoanDTO;
import com.ol.bankloan.dto.BasicResponseDTO;
import com.ol.bankloan.enums.LoanStatusEnum;
import com.ol.bankloan.enums.UserRoleEnum;
import com.ol.bankloan.models.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/employee")
public class EmployeeController {
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

    private final ModelMapper mapper = new ModelMapper();

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/approve-loan/{customerId}")
    public ResponseEntity<BasicResponseDTO<EMI>> approveLoan(@PathVariable("customerId") Long customerId){
        Optional<Customer> _customer = customerDAO.findById(customerId);
        if(_customer.isEmpty()){
            return new ResponseEntity<>(new BasicResponseDTO<>(false, "Customer not found", null), HttpStatus.OK);
        }
        Customer customer = _customer.get();
        customer.setLoanStatus(LoanStatusEnum.APPROVED);
        customerDAO.save(customer);
        Double rate = customer.getLoan().getRate() / 12 /100;
        Double nC = Math.pow(1+rate, customer.getLoan().getDuration());
        Double emi = customer.getLoanAmount() * rate * nC / (nC-1);
        EMI emiM = new EMI();
        emiM.setEmiAmount(emi);
        emiM.setCustomer(customer);
        emiM.setTotalAmount(customer.getLoanAmount());
        emiM.setIntAmount( ((emi * customer.getLoan().getDuration()) - customer.getLoanAmount()) / customer.getLoan().getDuration() );
        emiDAO.save(emiM);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "", emiM), HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/view-all-loans")
    public ResponseEntity<BasicResponseDTO<List<Customer>>> viewLoanApplications(){
        List<Customer> customers = customerDAO.findAll();
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "All records", customers), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/view-all-borrowers")
    public ResponseEntity<BasicResponseDTO<List<EMI>>> viewBorrowers(){
        List<EMI> emis = emiDAO.findAll();
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "All records", emis), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/customer-list")
    public ResponseEntity<BasicResponseDTO<List<Customer>>> viewCustomers(){
        List<Customer> customers = customerDAO.findAll();
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "All records", customers), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/users-list")
    public ResponseEntity<BasicResponseDTO<List<User>>> viewUsers(){
        List<User> users = userDAO.findAllByRole(UserRoleEnum.CUSTOMER);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "All records", users), HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/remove-customer/{customerId}")
    @Transactional
    public ResponseEntity<BasicResponseDTO<?>> removeCustomer( @PathVariable("customerId") Long customerId){
        Optional<Customer> _cust = customerDAO.findById(customerId);
        if(_cust.isEmpty()){
            return new ResponseEntity<>(new BasicResponseDTO<>(false, "Customer not found", null), HttpStatus.OK);
        }
        Customer customer = _cust.get();
        emiDAO.deleteByCustomer(customer);
        paymentDAO.deleteByCustomer(customer);
        customerDAO.deleteById(customerId);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Deleted successfully", null), HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/make-payment")
    public ResponseEntity<BasicResponseDTO<Payment>> makePayment(@RequestParam("customerId") Long customerId, @RequestParam("emiId") Long emiId){
        Optional<Customer> _customer = customerDAO.findById(customerId);
        Optional<EMI> _emi = emiDAO.findById(emiId);
        if(_customer.isPresent() && _emi.isPresent()){
            Payment payment = new Payment();
            EMI emi = _emi.get();
            payment.setCustomer(_customer.get());
            payment.setPaymentNumber(UUID.randomUUID().toString());
            payment.setEmiDate(new Date());
            payment.setReciptDate(new Date());
            payment.setLateCharge(0.0);
            payment.setEmiAmount(emi.getEmiAmount());
            payment.setTotalAmount(emi.getEmiAmount());
            paymentDAO.save(payment);
            return new ResponseEntity<>(new BasicResponseDTO<>(true, "Payment successfully added", payment), HttpStatus.OK);
        }
        return new ResponseEntity<>(new BasicResponseDTO<>(false, "Payment or EMI not found", null), HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/add-customer")
    public ResponseEntity<BasicResponseDTO<Customer>> applyForLoan( @RequestBody ApplyLoanDTO applyLoanDTO){
        Customer customer = this.mapper.map(applyLoanDTO, Customer.class);
        Optional<Loan> _loan = loanDAO.findById(applyLoanDTO.getLoanId());
        Optional<User> _user = userDAO.findById(applyLoanDTO.getUser_id());
        if(_user.isPresent() && _loan.isPresent()){
            if(!addressDAO.existsByUser(_user.get())){
                return new ResponseEntity<>(new BasicResponseDTO<>(false, "Please add address details first before apply for loan", null), HttpStatus.OK);
            }
            customer.setLoanStatus(LoanStatusEnum.PENDING);
            customer.setUser(_user.get());
            customer.setLoan(_loan.get());
            customerDAO.save(customer);
            return new ResponseEntity<>(new BasicResponseDTO<>(true, "Customer Added", customer), HttpStatus.OK);
        }
        return new ResponseEntity<>(new BasicResponseDTO<>(false, "User or loan not found", null), HttpStatus.OK);
    }
}
