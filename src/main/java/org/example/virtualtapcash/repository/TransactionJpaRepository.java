package org.example.virtualtapcash.repository;


import org.example.virtualtapcash.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT * FROM tb_transaction WHERE card_id = ?1", nativeQuery = true)
    List<Transaction> findTransactionsByCardId(String cardId);

}