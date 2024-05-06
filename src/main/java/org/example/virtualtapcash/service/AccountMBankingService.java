package org.example.virtualtapcash.service;

import org.example.virtualtapcash.dto.general.response.ApiResponseDto;
import org.example.virtualtapcash.model.MBankingAccount;
import org.example.virtualtapcash.repository.AccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountMBankingService {
    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Autowired
    private PasswordEncoder encoder;

    public ApiResponseDto getUserByUsername(String username) {
        Optional<MBankingAccount> accountOptional = accountJpaRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            return new ApiResponseDto("success", accountOptional.get(), "User found");
        } else {
            return new ApiResponseDto("error", null, "Account not found");
        }
    }

}
