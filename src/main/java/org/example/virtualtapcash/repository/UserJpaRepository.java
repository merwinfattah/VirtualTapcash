package org.example.virtualtapcash.repository;

import org.example.virtualtapcash.models.MBankingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<MBankingAccount, String> {

}
