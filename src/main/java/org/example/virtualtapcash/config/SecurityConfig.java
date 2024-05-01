package org.example.virtualtapcash.config;
import org.example.virtualtapcash.filter.JwtAuthFilter;
import org.example.virtualtapcash.repository.AccountJpaRepository;
import org.example.virtualtapcash.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthFilter authFilter;
    public SecurityConfig(JwtAuthFilter authFilter) {
        this.authFilter = authFilter;
    }
    // User Creation
    @Bean
    public UserDetailsService userDetailsService(AccountJpaRepository repository, PasswordEncoder passwordEncoder) {
        return new CustomUserDetailsService(repository, passwordEncoder);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {


        return http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login", "/api/v1/card/get-card/**", "api/v1/transaction/payment").permitAll()
                        .requestMatchers("/api/v1/auth/hello", "/api/v1/account/get-user-data/**", "/api/v1/account/authorize-qr", "/api/v1/card/get-cards-data/**", "/api/v1/card/add-card", "/api/v1/card/add-card2", "/api/v1/card/remove-card/", "/api/v1/transaction/get-transaction-data/**", "api/v1/transaction/top-up-n-withdraw").authenticated()

                )
                .httpBasic(withDefaults()).csrf((csrf) -> csrf.disable())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}