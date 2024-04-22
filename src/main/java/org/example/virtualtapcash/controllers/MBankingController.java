package org.example.virtualtapcash.controllers;

import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.services.MBankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/account")
public class MBankingController {

    @Autowired
    private MBankingService mBankingService;

//    @PostMapping("/login")
//    public ResponseEntity<MBankingAccount> loginUser(@RequestBody MBankingAccount user) {
//
//    }
}
