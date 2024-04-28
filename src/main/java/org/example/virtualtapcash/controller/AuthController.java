package org.example.virtualtapcash.controller;

import org.example.virtualtapcash.dto.account.request.AccountRegisterDto;
import org.example.virtualtapcash.dto.account.request.AuthDto;
import org.example.virtualtapcash.security.CustomUserDetailsService;
import org.example.virtualtapcash.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ResponseEntity<String> addNewUser(@RequestBody AccountRegisterDto userInfo) {
        String response = service.addUser(userInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthDto authDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPin()));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(authDto.getUsername());
                return ResponseEntity.ok(token);
            } else {
                throw new BadCredentialsException("Authentication failed for user: " + authDto.getUsername());
            }
        } catch (AuthenticationException e) {
            // Log the authentication exception for debugging
            throw new BadCredentialsException("Authentication failed for user: " + authDto.getUsername());
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {

        String response = "Hello World";
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


//
}
