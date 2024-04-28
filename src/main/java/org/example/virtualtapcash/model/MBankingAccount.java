package org.example.virtualtapcash.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_mbanking_account",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"virtual_tapcash_id"}), @UniqueConstraint(columnNames = {"account_number"})})

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MBankingAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "virtual_tapcash_id")
    private String virtualTapCashId;

    @Column(name = "pin")
    private String pin;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bank_account_balance")
    private BigDecimal bankAccountBalance;

    @Column(name = "role")
    private String role;

}
