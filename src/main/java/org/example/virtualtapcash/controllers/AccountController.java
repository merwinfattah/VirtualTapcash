package org.example.virtualtapcash.controllers;

import org.example.virtualtapcash.services.MBankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private MBankingService mBankingService;

//    @GetMapping("/get-user-data")
//    public Optional <MBankingAccount> getAccountnDetails() {
//        String name = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return mBankingService.
//    }
}
