package org.example.virtualtapcash.controllers;

import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.repository.UserJpaRepository;
import org.example.virtualtapcash.services.MBankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public ResponseEntity<MBankingAccount> getUserData(@PathVariable Long userId) {
        return mBankingService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create-user-data")
    public ResponseEntity<MBankingAccount> createAccount(@RequestBody MBankingAccount user) {
        MBankingAccount createdUser = mBankingService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/update-user-data/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody MBankingAccount user) {
        try {
            Optional<MBankingAccount> updatedUser = mBankingService.updateUser(userId, user);
            if (updatedUser.isPresent()) {
                return ResponseEntity.ok(updatedUser.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the account.");
        }
    }

    @DeleteMapping("/delete-user-data/{userId}")
    public ResponseEntity<Void> deleteUserData(@PathVariable Long userId) {
        try {
            mBankingService.deleteUser(userId);
            return ResponseEntity.ok().build(); // Return 200 OK to indicate successful deletion
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if the user doesn't exist
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Return 500 Internal Server Error for any other exceptions
        }
    }

}
