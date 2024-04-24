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

    @GetMapping("/getuserdata/{userId}")
    public ResponseEntity<Optional<MBankingAccount>> getUserData(@PathVariable Long userId) {
        Optional<MBankingAccount> response = mBankingService.getUserById(userId);
        if(response.isPresent()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}

//    @GetMapping("/get-user-data")
//    public Optional <MBankingAccount> getAccountnDetails() {
//        String name = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return mBankingService.
//    }
//}
