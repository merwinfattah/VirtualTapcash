package org.example.virtualtapcash.services;




import org.example.virtualtapcash.entities.TransactionResult;
import org.example.virtualtapcash.models.TapcashCard;
import org.example.virtualtapcash.models.Transaction;
import org.example.virtualtapcash.repository.TapcashCardJpaRepository;
import org.example.virtualtapcash.repository.TransactionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.virtualtapcash.exceptions.CardNotFoundException;
import org.example.virtualtapcash.exceptions.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionJpaRepository transactionJpaRepository;
    @Autowired
    private TapcashCardJpaRepository tapcashCardJpaRepository;


    public Transaction createTransaction(Transaction transaction) { return transactionJpaRepository.save(transaction);
    }

    public List<Transaction> getAllTransaction() { return transactionJpaRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long transactionId) { return transactionJpaRepository.findById(transactionId);
    }

    public void deleteTransaction(Long transactionId) {
        transactionJpaRepository.deleteById(transactionId);
    }

    public Transaction updateTransaction (Transaction transaction) { return transactionJpaRepository.save(transaction);
    }


    @Transactional
    public TransactionResult processPayment(String rfid, BigDecimal nominal) {
        // Retrieve the TapcashCard by RFID
        TapcashCard card = tapcashCardJpaRepository.getCardByRfid(rfid)
                .orElseThrow(() -> new CardNotFoundException("Card not found with RFID: " + rfid));

        // Check if the card has sufficient balance
        if (card.getTapCashBalance().compareTo(nominal) < 0) {
            throw new InsufficientFundsException("Insufficient funds available.");
        }

        // Deduct the transaction amount from the card's balance
        card.setTapCashBalance(card.getTapCashBalance().subtract(nominal));
        tapcashCardJpaRepository.save(card);

        // Record the transaction
        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setNominal(nominal);
        transaction.setCreatedAt(new Date()); // Ensure you set all required fields
        transactionJpaRepository.save(transaction);

        // Return a success result
        return new TransactionResult(true, "Payment successful");
    }

}