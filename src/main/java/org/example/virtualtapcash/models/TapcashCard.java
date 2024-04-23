package org.example.virtualtapcash.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table( name = "tb_tapcash_card")
public class TapcashCard {
    @Override
    public String toString() {
        return "TapcashCard{" +
                "rfid='" + rfid + '\'' +
                ", tapCashBalance=" + tapCashBalance +
                ", isDefault=" + isDefault +
                ", name='" + name + '\'' +
                ", registeredAt=" + registeredAt +
                ", updatedAt=" + updatedAt +
                ", status='" + status + '\'' +
                ", user=" + user +
                '}';
    }

    @Id
    private String rfid;

    @Column(name = "tapcash_balance")
    private BigDecimal tapCashBalance;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "name")
    private String name;

    @Column(name = "registered_at")
    private Date registeredAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "virtual_tapcash_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MBankingAccount user;

    public TapcashCard() {
    }

    public  TapcashCard(String rfid, BigDecimal tapCashBalance, Boolean isDefault, String name, Date registeredAt, Date updatedAt, String status, MBankingAccount user) {
        this.rfid = rfid;
        this.tapCashBalance = tapCashBalance;
        this.isDefault = isDefault;
        this.name = name;
        this.registeredAt = registeredAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.user = user;
    }

    @NonNull
    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public BigDecimal getTapCashBalance() {
        return tapCashBalance;
    }

    public void setTapCashBalance(BigDecimal tapCashBalance) {
        this.tapCashBalance = tapCashBalance;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MBankingAccount getUser() {
        return user;
    }

    public void setUser(MBankingAccount user) {
        this.user = user;
    }
}
