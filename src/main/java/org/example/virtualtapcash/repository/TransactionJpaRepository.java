package org.example.virtualtapcash.repository;


import org.example.virtualtapcash.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {



}
