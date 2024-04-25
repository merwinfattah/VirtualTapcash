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


}