package org.example.virtualtapcash.services;




import org.example.virtualtapcash.models.Transaction;
import org.example.virtualtapcash.repository.TransactionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionJpaRepository TransactionJpaRepository;



    public Transaction createdTransaction(Transaction transaction) { return TransactionJpaRepository.save(transaction);
    }

    public List<Transaction> getAllTransaction() { return TransactionJpaRepository.findAll();
    }

    public Optional<Transaction> getTransactionId(String transactionId) { return TransactionJpaRepository.findById(transactionId);
    }

    public void deleteTransaction(String transactionId) {
        TransactionJpaRepository.deleteById(transactionId);
    }

    public Transaction updatedTransaction (Transaction transaction) { return TransactionJpaRepository.save(transaction);
    }



}