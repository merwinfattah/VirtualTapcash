package org.example.virtualtapcash.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.example.virtualtapcash.entities.Role;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "tb_mbanking_account")
public class MBankingAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "virtual_tapcash_id")
    private String virtualTapCashId;

    @Column(name = "m_pin")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String mPin;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bank_account_balance")
    private BigDecimal bankAccountBalance;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles = new ArrayList<>();



    public MBankingAccount() {
    }

    public MBankingAccount(long userId, String username, String virtualTapCashId, String mPin, String customerName, String accountNumber, BigDecimal bankAccountBalance) {
        this.userId = userId;
        this.username = username;
        this.virtualTapCashId = virtualTapCashId;
        this.mPin = mPin;
        this.customerName = customerName;
        this.accountNumber = accountNumber;
        this.bankAccountBalance = bankAccountBalance;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVirtualTapCashId() {
        return virtualTapCashId;
    }

    public void setVirtualTapCashId(String virtualTapCashId) {
        this.virtualTapCashId = virtualTapCashId;
    }

    public String getmPin() {
        return mPin;
    }

    public void setmPin(String mPin) {
        this.mPin = mPin;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBankAccountBalance() {
        return bankAccountBalance;
    }

    public void setBankAccountBalance(BigDecimal bankAccountBalance) {
        this.bankAccountBalance = bankAccountBalance;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "MBankingAccount{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", virtualTapCashId='" + virtualTapCashId + '\'' +
                ", mPin='" + mPin + '\'' +
                ", customerName='" + customerName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", bankAccountBalance=" + bankAccountBalance +
                '}';
    }
}
