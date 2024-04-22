package org.example.virtualtapcash.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Entity
@Table( name = "tb_mbankingAccount")
public class MBankingAccount {
    @Id
    private String userId;

    @Column(name = "virtual_tapcash_id")
    private String virtualTapCashId;

    @Column(name = "m_pin")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String mPin;

    @Column(name = "name")
    private String name;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bank_account_balance")
    private BigDecimal bankAccountBalance;

    public MBankingAccount() {
    }

    public MBankingAccount(String userId, String virtualTapCashId, String mPin, String name, String accountNumber, BigDecimal bankAccountBalance) {
        this.userId = userId;
        this.virtualTapCashId = virtualTapCashId;
        this.mPin = mPin;
        this.name = name;
        this.accountNumber = accountNumber;
        this.bankAccountBalance = bankAccountBalance;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public String getVirtualTapCashId() {
        return virtualTapCashId;
    }

    @NonNull
    public String getmPin() {
        return mPin;
    }

    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBankAccountBalance() {
        return bankAccountBalance;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVirtualTapCashId(String virtualTapCashId) {
        this.virtualTapCashId = virtualTapCashId;
    }

    public void setmPin(String mPin) {
        this.mPin = mPin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBankAccountBalance(BigDecimal bankAccountBalance) {
        this.bankAccountBalance = bankAccountBalance;
    }


}
