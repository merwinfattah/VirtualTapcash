package org.example.virtualtapcash.controllers;

import org.example.virtualtapcash.entities.LoginCredentials;
import org.example.virtualtapcash.entities.RegisterDto;
import org.example.virtualtapcash.entities.Role;
import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.repository.RoleJpaRepository;
import org.example.virtualtapcash.repository.UserJpaRepository;
import org.example.virtualtapcash.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

    private UserJpaRepository userJpaRepository;

    private RoleJpaRepository roleJpaRepository;

    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;


    @Autowired
    public AuthController(UserJpaRepository userJpaRepository, RoleJpaRepository roleJpaRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userJpaRepository = userJpaRepository;
        this.roleJpaRepository = roleJpaRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerHandler(@RequestBody RegisterDto registerDto) {
        if (userJpaRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is taken", HttpStatus.BAD_REQUEST);
        }
            MBankingAccount user = new MBankingAccount();
            user.setUsername(registerDto.getUsername());
            user.setmPin(passwordEncoder.encode(registerDto.getMPin()));
            Role roles = roleJpaRepository.findByName("USER").get();
            user.setRoles(Collections.singletonList(roles));

            userJpaRepository.save(user);

            return new ResponseEntity<>("User registered success!", HttpStatus.OK);

    }

//    @PostMapping("/login")
//    public Map<String, Object> loginHandler(@RequestBody LoginCredentials body) {
//        try {
//            UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(body.getUserId(), body.getmPin());
//            authenticationManager.authenticate(authInputToken);
//            String token = jwtUtil.generateToken(body.getUserId());
//            return Collections.singletonMap("jwt-token", token);
//        } catch ( AuthenticationException authExc) {
//            throw new RuntimeException("Invalid Login Credentials");
//        }
//    }

}
