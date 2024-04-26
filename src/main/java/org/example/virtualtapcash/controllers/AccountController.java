package org.example.virtualtapcash.controllers;

import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.repository.UserJpaRepository;
import org.example.virtualtapcash.services.MBankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/account")
public class    AccountController {

    @Autowired
    private MBankingService mBankingService;

    @GetMapping("/get-user-data/{userId}")
    public ResponseEntity<?> getUserData(@PathVariable Long userId) {
        try {
            return mBankingService.getUserById(userId);
        } catch (Exception e) {
            String errorMessage = "An error occurred while retrieving card data.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);

        }

    }

    @PostMapping("/authorize-qr/{pin}")
    public ResponseEntity<?> authorizeQr(@PathVariable Long userId, @RequestParam String pin) {
        try {
            return mBankingService.verifyQr(userId, pin);
        } catch (Exception e) {
            String errorMessage = "An error occurred while retrieving card data.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

}
