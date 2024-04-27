package org.example.virtualtapcash.repositories;


import org.example.virtualtapcash.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT * FROM tb_transaction WHERE rfid = ?1", nativeQuery = true)
    List<Transaction> findTransactionsByRfid(String rfid);

}
