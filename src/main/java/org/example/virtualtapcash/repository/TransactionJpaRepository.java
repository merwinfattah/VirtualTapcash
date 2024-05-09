package org.example.virtualtapcash.repository;


import org.example.virtualtapcash.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT * FROM tb_transaction WHERE card_id = ?1 AND virtual_tapcash_id = ?2 ORDER BY created_at DESC", nativeQuery = true)
    List<Transaction> findTransactionsByCardIdAndVirtualTapcashId(String cardId, String virtualTapcashID);


}