package org.example.virtualtapcash.security;

import org.example.virtualtapcash.entities.AccountRegisterRequest;
import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.repository.UserJpaRepository;
import org.example.virtualtapcash.security.UserInfoDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    private final PasswordEncoder encoder;

    public CustomUserDetailsService(UserJpaRepository userJpaRepository, PasswordEncoder encoder) {
        this.userJpaRepository = userJpaRepository;
        this.encoder = encoder;
    }

    private String generateAccountNumber() {
        StringBuilder accountNumber = new StringBuilder("ACCT-");
        // You can customize the format of the account number based on your bank's requirements
        // For example, you can add random digits to the account number
        for (int i = 0; i < 8; i++) {
            accountNumber.append((int) (Math.random() * 10));
        }
        return accountNumber.toString();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MBankingAccount> userDetail = userJpaRepository.findByUsername(username);
        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
    public String addUser(AccountRegisterRequest userInfo) {
        if (userInfo.getPin() != null) {
            if (!userJpaRepository.existsByUsername(userInfo.getUsername())) {
                MBankingAccount newUser = new MBankingAccount();
                newUser.setUsername(userInfo.getUsername());
                newUser.setPin(encoder.encode(userInfo.getPin()));
                newUser.setCustomerName(userInfo.getCustomerName());

                String accountNumber = generateAccountNumber();
                String virtualTapCashId = UUID.randomUUID().toString();
                BigDecimal minBalance = BigDecimal.ZERO;
                BigDecimal maxBalance = new BigDecimal("2000000"); // 2 million
                BigDecimal randomBalance = minBalance.add(new BigDecimal(Math.random()).multiply(maxBalance.subtract(minBalance)));

                newUser.setBankAccountBalance(randomBalance.setScale(2, RoundingMode.HALF_UP));
                newUser.setAccountNumber(accountNumber);
                newUser.setVirtualTapCashId(virtualTapCashId);
                newUser.setRole("USER");

                userJpaRepository.save(newUser);
                return "User Added Successfully";
            }

            return  "Username has been used";

        } else {
            return "Invalid password provided";
        }
    }




}
