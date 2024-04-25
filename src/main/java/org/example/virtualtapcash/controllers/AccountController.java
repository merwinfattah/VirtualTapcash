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
public class AccountController {

    @Autowired
    private MBankingService mBankingService;

    @GetMapping("/get-user-data/{userId}")
    public ResponseEntity<Optional<MBankingAccount>> getUserData(@PathVariable Long userId) {
        Optional<MBankingAccount> response = mBankingService.getUserById(userId);
        if(response.isPresent()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete-user-data/{userId}")
    public ResponseEntity<Void> deleteUserData(@PathVariable Long userId) {
        Optional<MBankingAccount> existingUser = mBankingService.getUserById(userId);
        if (existingUser.isPresent()) {
            mBankingService.deleteUser(userId);
            return ResponseEntity.ok().build(); // Return 200 OK to indicate successful deletion
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if the user doesn't exist
        }
    }

    @PostMapping("/create-user-data")
    public ResponseEntity<MBankingAccount> createAccount(@RequestBody MBankingAccount user) {
        if (user != null) {
            MBankingAccount createdUser = mBankingService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update-user-data/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody MBankingAccount user) {
        // Check if the user ID in the path matches the user ID in the request body
        if (user.getUserId() != userId) {
            return ResponseEntity.badRequest().body("Mismatched user ID in the request path and body.");
        }

        // Check if the user exists before attempting an update
        if (!mBankingService.getUserById(userId).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            MBankingAccount updatedUser = mBankingService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the account: " + e.getMessage());
        }
    }

}
