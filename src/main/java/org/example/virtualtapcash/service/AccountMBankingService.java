package org.example.virtualtapcash.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
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
            try {
                // Create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                // Create and configure SimpleFilterProvider
                SimpleFilterProvider filterProvider = new SimpleFilterProvider();
                filterProvider.addFilter("userFilter", SimpleBeanPropertyFilter.serializeAllExcept("pin"));

                // Set the filter provider to the ObjectMapper
                objectMapper.setFilterProvider(filterProvider);

                // Convert transactions to JSON using the ObjectMapper
                String userJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(accountOptional.get());

                return new ApiResponseDto("success", userJson, "User found");
            } catch (Exception e) {
                throw new RuntimeException("Error converting transactions to JSON: " + e.getMessage());
            }
        } else {
            return new ApiResponseDto("error", null, "Account not found");
        }
    }

}
