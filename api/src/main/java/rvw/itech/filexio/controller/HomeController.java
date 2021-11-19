/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvw.itech.filexio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rvw.itech.filexio.model.JwtRequest;
import rvw.itech.filexio.model.JwtResponse;
import rvw.itech.filexio.security.JWTUtility;
import rvw.itech.filexio.service.UserService;

/**
 *
 * @author renj
 */
@RestController
@CrossOrigin
public class HomeController {
    
    @Autowired
    private UserService userService; 
    @Autowired
    private JWTUtility jwtTokenUtil; 
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @GetMapping("/")
    public ResponseEntity<String> getHomePage(){
        String message = "Welcome to Filexio!";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    
    @PostMapping("/authenticate")
    public JwtResponse createAuthenticationToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        try{
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getUsername(), 
                        jwtRequest.getPassword())
            );
        }catch(BadCredentialsException e){
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        System.out.println("request username " + jwtRequest.getUsername());
        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
        final String jwtToken = jwtTokenUtil.generateToken(userDetails);
        
        return new JwtResponse(jwtToken);
    }
}
