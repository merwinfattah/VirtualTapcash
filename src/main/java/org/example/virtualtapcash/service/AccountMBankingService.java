package org.example.virtualtapcash.service;

import org.example.virtualtapcash.model.MBankingAccount;
import org.example.virtualtapcash.repository.AccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountMBankingService {
    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<?> getUserByUsername(String username) {
        Optional<MBankingAccount> response = accountJpaRepository.findByUsername(username);
        if(response.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
    }

    public ResponseEntity<?> verifyQr(Long userId, String pin) {
        Optional<MBankingAccount> response = accountJpaRepository.findById(userId);
        String hashedPin = encoder.encode(pin);
        if (response.get().getPin().equals(hashedPin)) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("Wrong pin number");
        }
    }




}
