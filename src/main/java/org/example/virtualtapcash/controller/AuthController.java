package org.example.virtualtapcash.controller;

import org.example.virtualtapcash.dto.account.request.AccountRegisterDto;
import org.example.virtualtapcash.dto.account.request.AuthDto;
import org.example.virtualtapcash.dto.general.response.ApiResponseDto;
import org.example.virtualtapcash.exception.account.AccountRegisteredException;
import org.example.virtualtapcash.exception.account.BadCredentialException;
import org.example.virtualtapcash.security.CustomUserDetailsService;
import org.example.virtualtapcash.service.JwtService;
import org.example.virtualtapcash.service.QrService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final CustomUserDetailsService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final QrService qrService;

    public AuthController(CustomUserDetailsService customUserDetailsService, JwtService jwtService, AuthenticationManager authenticationManager, QrService qrService) {
        this.service = customUserDetailsService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.qrService = qrService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> addNewUser(@RequestBody AccountRegisterDto userInfo) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.addUser(userInfo));
        } catch (BadCredentialException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDto("error", null, e.getMessage()));
        } catch (AccountRegisteredException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponseDto("error", null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto("error", null, e.getMessage()));
        }

    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto> authenticateAndGetToken(@RequestBody AuthDto authDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPin()));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(authDto.getUsername());

                // Call the QR service to deactivate QR codes associated with this user
                qrService.deactivateQrCodesForUser(authDto.getUsername());

                return ResponseEntity.ok(new ApiResponseDto("success", token, "login successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseDto("error", null, "Invalid credentials"));
            }
        } catch (AuthenticationException e) {
            // Log the authentication exception for debugging
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponseDto("error", null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto("error", null, e.getMessage()));
        }

    }

}
