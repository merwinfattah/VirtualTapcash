package org.example.virtualtapcash.controller;

import org.example.virtualtapcash.dto.transaction.request.PaymentDto;
import org.example.virtualtapcash.dto.transaction.request.TransactionDto;
import org.example.virtualtapcash.dto.transaction.response.TransactionResultDto;
import org.example.virtualtapcash.exception.transaction.ErrorTransaction;
import org.example.virtualtapcash.model.QR;
import org.example.virtualtapcash.model.Transaction;
import org.example.virtualtapcash.repository.QrJpaRepository;
import org.example.virtualtapcash.service.QrService;
import org.example.virtualtapcash.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.virtualtapcash.exception.card.CardNotFoundException;
import org.example.virtualtapcash.exception.transaction.InsufficientFundsException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private  TransactionService transactionService;

    @Autowired
    private QrService qrService;

    @GetMapping("/get-transaction-data/{cardId}")
    public ResponseEntity<?> getTransactionData(@PathVariable String cardId) {
        try {
            List<Transaction> transactions = transactionService.getTransactionByCardId(cardId);
            return ResponseEntity.ok(transactions);
        } catch (CardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            String errorMessage = "An error occurred while processing the request.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PostMapping("/payment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentDto paymentDto) {
        try {
            TransactionResultDto result = transactionService.processPayment(paymentDto.getCardId(), paymentDto.getNominal());
            return ResponseEntity.ok(result.getMessage());
        } catch (InsufficientFundsException | CardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    @PostMapping("/activate/{cardId}")
    public ResponseEntity<?> activateQrCode(@PathVariable String cardId) {
        boolean isActivated = qrService.activateQrCodeByCardId(cardId);
        if (isActivated) {
            return ResponseEntity.ok("QR Code activated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to activate QR Code or no inactive QR Code found.");
        }
    }

    @PostMapping("/deactivate/{cardId}")
    public ResponseEntity<String> deactivateQrCodeImmediately(@PathVariable String cardId) {
        boolean isDeactivated = qrService.deactivateQrCodeByCardIdImmediately(cardId);
        if (isDeactivated) {
            return ResponseEntity.ok("QR Code deactivated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("QR Code not found or already inactive.");
        }
    }


    @PostMapping("/qrpayment/{cardId}")
    public ResponseEntity<?> qrPayment(@PathVariable String cardId, @RequestBody PaymentDto paymentDto) {
        try {
            // Check if there's an active QR code for the card
            boolean isActive = qrService.checkIsActiveByCardId(cardId);
            if (!isActive) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No active QR Code associated with this card or QR Code is not found.");
            }

            // Proceed with payment
            TransactionResultDto result = transactionService.processPayment(paymentDto.getCardId(), paymentDto.getNominal());

            // Deactivate the QR code immediately after successful transaction
            qrService.deactivateQrCodeByCardId(cardId);

            return ResponseEntity.ok(result.getMessage());
        } catch (InsufficientFundsException | CardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ErrorTransaction e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transaction error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the transaction.");
        }
    }




    @PostMapping("/top-up-n-withdraw")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDto transactionDto) {
        try {
            TransactionResultDto result = transactionService.handleTopUpWithdrawal(transactionDto.getCardId(), transactionDto.getNominal(), transactionDto.getType(), transactionDto.getVirtual_tapcash_id(), transactionDto.getPin());
            return ResponseEntity.ok(result.getMessage());
        } catch (ErrorTransaction | CardNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

}