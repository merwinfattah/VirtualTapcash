package org.example.virtualtapcash.controller;

import org.example.virtualtapcash.dto.account.request.AuthorizeQrDto;
import org.example.virtualtapcash.dto.general.response.ApiResponseDto;
import org.example.virtualtapcash.service.JwtService;
import org.example.virtualtapcash.service.AccountMBankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
public class    AccountController {

    @Autowired
    private AccountMBankingService accountMBankingService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/get-user-data")
    public ResponseEntity<ApiResponseDto> getUserData(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            String username = jwtService.extractUsername(jwtToken);
            return ResponseEntity.ok(accountMBankingService.getUserByUsername(username));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto("error", null, errorMessage));
        }
    }

    @PostMapping("/authorize-qr")
    public ResponseEntity<ApiResponseDto> authorizeQr(@RequestBody AuthorizeQrDto request) {
        try {
            return ResponseEntity.ok(accountMBankingService.verifyQr(request.getUserId(), request.getPin()));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDto("error", null, errorMessage));
        }
    }

}
