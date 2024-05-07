package org.example.virtualtapcash.repository;

import org.example.virtualtapcash.model.MBankingAccount;
import org.example.virtualtapcash.model.TapcashCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface AccountJpaRepository extends JpaRepository<MBankingAccount, Long> {
    @Query(value = "SELECT * FROM tb_mbanking_acount WHERE virtual_tapcash_id =?1", nativeQuery = true)
    Optional<MBankingAccount> getUserByVirtualTapcashId(String virtualTapcashId);

    Optional<MBankingAccount> findByUsername(String name);

    Optional<MBankingAccount> findMBankingAccountByVirtualTapCashId(String virtualTapcashId);

    Boolean existsByUsername(String username);
}
