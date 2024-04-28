package org.example.virtualtapcash.controller;

import org.example.virtualtapcash.service.JwtService;
import org.example.virtualtapcash.service.AccountMBankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
public class    AccountController {

    @Autowired
    private AccountMBankingService accountMBankingService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/get-user-data")
    public ResponseEntity<?> getUserData(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            String username = jwtService.extractUsername(jwtToken);
            return accountMBankingService.getUserByUsername(username);
        } catch (Exception e) {
            String errorMessage = "An error occurred while retrieving card data.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);

        }

    }

    @PostMapping("/authorize-qr/{pin}")
    public ResponseEntity<?> authorizeQr(@PathVariable Long userId, @RequestParam String pin) {
        try {
            return accountMBankingService.verifyQr(userId, pin);
        } catch (Exception e) {
            String errorMessage = "An error occurred while retrieving card data.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

}
