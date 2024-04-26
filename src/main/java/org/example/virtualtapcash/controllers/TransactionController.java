package org.example.virtualtapcash.controllers;

import org.example.virtualtapcash.entities.PaymentRequest;
import org.example.virtualtapcash.entities.TransactionRequest;
import org.example.virtualtapcash.entities.TransactionResult;
import org.example.virtualtapcash.exceptions.ErrorTransaction;
import org.example.virtualtapcash.models.Transaction;
import org.example.virtualtapcash.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.virtualtapcash.exceptions.CardNotFoundException;
import org.example.virtualtapcash.exceptions.InsufficientFundsException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private  TransactionService transactionService;

    @GetMapping("/get-transaction-data/{rfid}")
    public ResponseEntity<?> getTransactionData(@PathVariable String rfid) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByRfid(rfid);
            return ResponseEntity.ok(transactions);
        } catch (CardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            String errorMessage = "An error occurred while processing the request.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PostMapping("/payment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            TransactionResult result = transactionService.processPayment(paymentRequest.getRfid(), paymentRequest.getNominal());
            return ResponseEntity.ok(result.getMessage());
        } catch (InsufficientFundsException | CardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    @PostMapping("/top-up-n-withdraw")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        try {
            TransactionResult result = transactionService.handleTopUpWithdrawal(transactionRequest.getRfid(), transactionRequest.getNominal(), transactionRequest.getType(), transactionRequest.getVirtual_tapcash_id(), transactionRequest.getPin());
            return ResponseEntity.ok(result.getMessage());
        } catch (ErrorTransaction | CardNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

}