package org.example.virtualtapcash.repositories;

import org.example.virtualtapcash.models.MBankingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface AccountJpaRepository extends JpaRepository<MBankingAccount, Long> {
    @Query("SELECT t FROM MBankingAccount t WHERE t.virtualTapCashId = :virtual_tapcash_id")
    Optional<MBankingAccount> getUserByVirtualTapcashId(@Param("virtual_tapcash_id") String virtual_tapcash_id);
    Optional<MBankingAccount> findByUsername(String name);

    Optional<MBankingAccount> findMBankingAccountByVirtualTapCashId(String virtualTapcashId);

    Boolean existsByUsername(String username);
}
