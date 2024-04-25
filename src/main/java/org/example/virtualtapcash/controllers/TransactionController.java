package org.example.virtualtapcash.controllers;

import org.example.virtualtapcash.entities.PaymentRequest;
import org.example.virtualtapcash.entities.TransactionResult;
import org.example.virtualtapcash.models.Transaction;
import org.example.virtualtapcash.repository.TransactionJpaRepository;
import org.example.virtualtapcash.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.example.virtualtapcash.exceptions.CardNotFoundException;
import org.example.virtualtapcash.exceptions.InsufficientFundsException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    private TransactionJpaRepository transactionJpaRepository;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/get-transaction-data/{rfid}")
    public ResponseEntity<?> getTransactionData(@PathVariable String rfid) {
        Optional<Transaction> transaction = transactionJpaRepository.findDataByRfid(rfid);
        if(transaction.isPresent()) {
            return ResponseEntity.ok(transaction.get());
        } else {
            return ResponseEntity.notFound().build();
        }
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