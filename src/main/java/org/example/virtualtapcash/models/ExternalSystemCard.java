package org.example.virtualtapcash.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tb_externalSystemCard")
public class ExternalSystemCard {
    @Override
    public String toString() {
        return "ExternalSystemCard{" +
                "cardId='" + cardId + '\'' +
                ", tapCashBalance=" + tapCashBalance +
                ", rfid=" + rfid +
                '}';
    }

    @Id
    private String cardId;

    @Column(name = "tapcash_balance")
    private BigDecimal tapCashBalance;

    @OneToOne
    @JoinColumn(name = "rfid")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TapcashCard rfid;

    public ExternalSystemCard() {
    }

    public ExternalSystemCard(BigDecimal tapCashBalance, TapcashCard rfid) {
        this.cardId = UUID.randomUUID().toString();
        this.tapCashBalance = tapCashBalance;
        this.rfid = rfid;
    }

    @NonNull
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @NonNull
    public BigDecimal getTapCashBalance() {
        return tapCashBalance;
    }

    public void setTapCashBalance(BigDecimal tapCashBalance) {
        this.tapCashBalance = tapCashBalance;
    }

    @NonNull
    public TapcashCard getRfid() {
        return rfid;
    }

    public void setRfid(TapcashCard rfid) {
        this.rfid = rfid;
    }
}
