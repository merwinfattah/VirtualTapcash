package org.example.virtualtapcash.controllers;

import org.example.virtualtapcash.entities.PaymentRequest;
import org.example.virtualtapcash.entities.TransactionRequest;
import org.example.virtualtapcash.exceptions.ErrorTransaction;
import org.example.virtualtapcash.models.MBankingAccount;
import org.example.virtualtapcash.models.TapcashCard;
import org.example.virtualtapcash.models.Transaction;
import org.example.virtualtapcash.repository.TapcashCardJpaRepository;
import org.example.virtualtapcash.repository.TransactionJpaRepository;
import org.example.virtualtapcash.repository.UserJpaRepository;
import org.example.virtualtapcash.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.virtualtapcash.exceptions.CardNotFoundException;
import org.example.virtualtapcash.exceptions.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private  TransactionService transactionService;

    @Autowired
    private TransactionJpaRepository transactionJpaRepository;

    @Autowired
    private TapcashCardJpaRepository tapcashCardJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;



    @GetMapping("/get-transaction-data/{rfid}")
    public ResponseEntity<?> getTransactionData(@PathVariable String rfid) {
        try {
            List<Transaction> transaction = transactionJpaRepository.findTransactionsByRfid(rfid);
            if(transaction.isEmpty()) {
                return ResponseEntity.ok(transaction);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Transactions Data Found");
            }
        } catch (Exception e) {
            String errorMessage = "An error occurred while processing the request.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }

    }

    @PostMapping("/payment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            TapcashCard card = tapcashCardJpaRepository.findTapcashCardsByRfid(paymentRequest.getRfid())
                    .orElseThrow(() -> new CardNotFoundException("Card not found with RFID: " + paymentRequest.getRfid()));

            // Check if the card has sufficient balance
            // Assuming paymentRequest.getNominal() returns a BigDecimal
            BigDecimal minRequiredBalance = paymentRequest.getNominal().add(new BigDecimal("4000"));
            if (card.getTapCashBalance().compareTo(minRequiredBalance) <= 0) {
                throw new InsufficientFundsException("Insufficient funds available. A minimum balance of 4000 over the transaction amount is required.");
            }


            // Deduct the transaction amount from the card's balance
            card.setTapCashBalance(card.getTapCashBalance().subtract(paymentRequest.getNominal()));
            tapcashCardJpaRepository.save(card);

            // Record the transaction
            Transaction transaction = new Transaction();

            transaction.setCard(card);
            transaction.setNominal(paymentRequest.getNominal());
            transaction.setCreatedAt(new Date());
            transaction.setUser(card.getUser());

            transactionJpaRepository.save(transaction);

            // Return a success result
            return ResponseEntity.status(HttpStatus.OK).body("Payment Successfull");

        } catch (InsufficientFundsException e) {
            return ResponseEntity.badRequest().body("Insufficient funds");
        } catch (CardNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            String errorMessage = "An error occurred while processing the request.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PostMapping("/top-up-n-withdraw")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        try {
            TapcashCard card = tapcashCardJpaRepository.findTapcashCardsByRfid(transactionRequest.getRfid())
                    .orElseThrow(() -> new CardNotFoundException("Card not found with RFID: " + transactionRequest.getRfid()));

            MBankingAccount mBankingAccount = userJpaRepository.getUserByVirtualTapcashId(transactionRequest.getVirtual_tapcash_id())
                    .orElseThrow(() -> new CardNotFoundException("Card not found with Virtual Tapcash Id: " + transactionRequest.getVirtual_tapcash_id()));

            if ("TOPUP".equals(transactionRequest.getType())) {
                // Check if the card's balance will exceed the limit after adding the transaction amount
                BigDecimal totalBalanceAfterTopUp = card.getTapCashBalance().add(transactionRequest.getNominal());
                if (totalBalanceAfterTopUp.compareTo(BigDecimal.valueOf(2000000)) > 0) {
                    throw new ErrorTransaction("Top-up amount exceeds maximum limit.");
                }

                // Deduct the transaction amount from the user's bank account balance
                mBankingAccount.setBankAccountBalance(mBankingAccount.getBankAccountBalance().subtract(transactionRequest.getNominal()));
                userJpaRepository.save(mBankingAccount);

                // Add the transaction amount to the card's balance
                card.setTapCashBalance(card.getTapCashBalance().add(transactionRequest.getNominal()));
                tapcashCardJpaRepository.save(card);
            } else if ("WITHDRAW".equals(transactionRequest.getType())) {
                // Check if the card's balance will exceed the limit after subtracting the transaction amount
                BigDecimal totalWithdraw = card.getTapCashBalance().subtract(transactionRequest.getNominal());
                if (totalWithdraw.compareTo(BigDecimal.ZERO) < 0) {
                    throw new ErrorTransaction("Withdrawal amount exceeds available balance.");
                }

                // Subtract the transaction amount from the card's balance
                card.setTapCashBalance(card.getTapCashBalance().subtract(transactionRequest.getNominal()));
                tapcashCardJpaRepository.save(card);

                // Add the transaction amount to the user's bank account balance
                mBankingAccount.setBankAccountBalance(mBankingAccount.getBankAccountBalance().add(transactionRequest.getNominal()));
                userJpaRepository.save(mBankingAccount);
            }

            // Record the transaction
            Transaction transaction = new Transaction();
            transaction.setCard(card);
            transaction.setNominal(transactionRequest.getNominal());
            transaction.setCreatedAt(new Date()); // Ensure you set all required fields
            transactionJpaRepository.save(transaction);

            // Return a success result
            return ResponseEntity.status(HttpStatus.OK).body("Transaction Successfull");

        } catch (ErrorTransaction e) {
            return ResponseEntity.badRequest().body("ERROR TRANSACTION");
        } catch (CardNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            String errorMessage = "An error occurred while processing the request.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
}