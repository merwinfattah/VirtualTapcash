package org.example.virtualtapcash.security;

import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.repository.UserJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    private final PasswordEncoder encoder;

    public CustomUserDetailsService(UserJpaRepository userJpaRepository, PasswordEncoder encoder) {
        this.userJpaRepository = userJpaRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MBankingAccount> userDetail = userJpaRepository.findByUsername(username);
        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
    public String addUser(MBankingAccount userInfo) {
        if (userInfo.getPin() != null) {
            userInfo.setPin(encoder.encode(userInfo.getPin()));
            userJpaRepository.save(userInfo);
            return "User Added Successfully";
        } else {
            return "Invalid password provided";
        }
    }




}
