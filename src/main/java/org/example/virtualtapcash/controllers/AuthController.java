package org.example.virtualtapcash.controllers;

import org.example.virtualtapcash.entities.AuthRequest;
import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.security.CustomUserDetailsService;
import org.example.virtualtapcash.security.JwtService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final CustomUserDetailsService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(CustomUserDetailsService customUserDetailsService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.service = customUserDetailsService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@RequestBody MBankingAccount userInfo) {
        String response = service.addUser(userInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPin()));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(authRequest.getUsername());
                return ResponseEntity.ok(token);
            } else {
                throw new BadCredentialsException("Authentication failed for user: " + authRequest.getUsername());
            }
        } catch (AuthenticationException e) {
            // Log the authentication exception for debugging
            throw new BadCredentialsException("Authentication failed for user: " + authRequest.getUsername());
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {

        String response = "Hello World";
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


//
}
