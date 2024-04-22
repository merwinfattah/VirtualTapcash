package org.example.virtualtapcash.controllers;

import org.example.virtualtapcash.entities.LoginCredentials;
import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.repository.UserJpaRepository;
import org.example.virtualtapcash.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.Map;

@Controller
public class AuthController {
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody MBankingAccount user) {
        String encodedPass = passwordEncoder.encode(user.getPin());
        user.setmPin(encodedPass);
        user = userJpaRepository.save(user);
        String token = jwtUtil.generateToken(user.getUserId());
        return Collections.singletonMap("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials body) {
        try {
            UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(body.getUserId(), body.getmPin());
            authenticationManager.authenticate(authInputToken);
            String token = jwtUtil.generateToken(body.getUserId());
            return Collections.singletonMap("jwt-token", token);
        } catch ( AuthenticationException authExc) {
            throw new RuntimeException("Invalid Login Credentials");
        }
    }

}
