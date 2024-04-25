package org.example.virtualtapcash.controllers;

import org.example.virtualtapcash.entities.PaymentRequest;
import org.example.virtualtapcash.entities.TransactionResult;
import org.example.virtualtapcash.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.virtualtapcash.exceptions.CardNotFoundException;
import org.example.virtualtapcash.exceptions.InsufficientFundsException;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {


    private final TransactionService transactionService;
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/payment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            TransactionResult result = transactionService.processPayment(paymentRequest.getRfid(), paymentRequest.getNominal());
            return ResponseEntity.ok(result);
        } catch (InsufficientFundsException e) {
            return ResponseEntity.badRequest().body("Insufficient funds");
        } catch (CardNotFoundException e) {
            return ResponseEntity.notFound().build();
        } // Handle other exceptions as necessary
    }

}