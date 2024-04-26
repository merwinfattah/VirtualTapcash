package org.example.virtualtapcash.services;

import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MBankingService {
    @Autowired
    private UserJpaRepository userJpaRepository;

    public ResponseEntity<?> getUserById(Long userId) {
        Optional<MBankingAccount> response = userJpaRepository.findById(userId);
        if(response.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
    }


}
