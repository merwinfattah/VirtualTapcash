package org.example.virtualtapcash.repository;

import org.example.virtualtapcash.models.MBankingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<MBankingAccount, Long> {
    Optional<MBankingAccount> findByUsername(String name);

    Boolean existsByUsername(String username);
}
