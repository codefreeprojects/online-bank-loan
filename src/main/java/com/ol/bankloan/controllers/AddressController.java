package com.ol.bankloan.controllers;

import com.ol.bankloan.dao.AddressDAO;
import com.ol.bankloan.dto.AddAddressRequestDTO;
import com.ol.bankloan.models.Address;
import com.ol.bankloan.models.User;
import com.ol.bankloan.dao.UserDAO;
import com.ol.bankloan.dto.BasicResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    AddressDAO addressDAO;
    @Autowired
    UserDAO userDAO;

    private final ModelMapper mapper = new ModelMapper();
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<BasicResponseDTO<List<Address>>> allAddress(){
        List<Address> addresses = addressDAO.findAll();
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Address saved", addresses), HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/save/{userId}")
    public ResponseEntity<BasicResponseDTO<Address>> saveAddress(@RequestBody AddAddressRequestDTO addressDto, @PathVariable("userId") Long userId){
        Address address = this.mapper.map(addressDto, Address.class);
        Optional<User> _user = userDAO.findById(userId);
        if(_user.isPresent()){
            if(addressDAO.existsByUser(_user.get())){
                return new ResponseEntity<>(new BasicResponseDTO<>(false, "Address for this user already exists", null), HttpStatus.CONFLICT);
            }
           address.setUser(_user.get());

        }
        addressDAO.save(address);
        return new ResponseEntity<>(new BasicResponseDTO<>(true, "Address saved", address), HttpStatus.CREATED);
    }
}
