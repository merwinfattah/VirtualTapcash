package org.example.virtualtapcash.repository;

import org.example.virtualtapcash.models.MBankingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<MBankingAccount, Long> {
    Optional<MBankingAccount> findByUsername(String name);

    Boolean existsByUsername(String username);
}
