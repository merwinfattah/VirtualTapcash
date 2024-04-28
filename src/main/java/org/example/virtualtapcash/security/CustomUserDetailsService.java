package org.example.virtualtapcash.security;

import org.example.virtualtapcash.dto.account.request.AccountRegisterDto;
import org.example.virtualtapcash.model.MBankingAccount;
import org.example.virtualtapcash.repository.AccountJpaRepository;
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

    private final AccountJpaRepository accountJpaRepository;

    private final PasswordEncoder encoder;

    public CustomUserDetailsService(AccountJpaRepository accountJpaRepository, PasswordEncoder encoder) {
        this.accountJpaRepository = accountJpaRepository;
        this.encoder = encoder;
    }

    private String generateAccountNumber() {
        StringBuilder accountNumber = new StringBuilder("");
        // You can customize the format of the account number based on your bank's requirements
        // For example, you can add random digits to the account number
        for (int i = 0; i < 8; i++) {
            accountNumber.append((int) (Math.random() * 10));
        }
        return accountNumber.toString();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MBankingAccount> userDetail = accountJpaRepository.findByUsername(username);
        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
    public String addUser(AccountRegisterDto userInfo) {
        if (userInfo.getPin() != null) {
            if (!accountJpaRepository.existsByUsername(userInfo.getUsername())) {
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

                accountJpaRepository.save(newUser);
                return "User Added Successfully";
            }

            return  "Username has been used";

        } else {
            return "Invalid password provided";
        }
    }




}
