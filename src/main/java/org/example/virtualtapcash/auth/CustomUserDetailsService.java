package org.example.virtualtapcash.auth;

import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional <MBankingAccount> userRes = userJpaRepository.findById(id);
        MBankingAccount user = userRes.orElseThrow(() -> new UsernameNotFoundException("Could not find user with id: " + id));
        return new org.springframework.security.core.userdetails.User(
                id,
                user.getPin(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
