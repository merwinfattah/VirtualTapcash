package org.example.virtualtapcash.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

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

    public ExternalSystemCard(String cardId, BigDecimal tapCashBalance, TapcashCard rfid) {
        this.cardId = cardId;
        this.tapCashBalance = tapCashBalance;
        this.rfid = rfid;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public BigDecimal getTapCashBalance() {
        return tapCashBalance;
    }

    public void setTapCashBalance(BigDecimal tapCashBalance) {
        this.tapCashBalance = tapCashBalance;
    }

    public TapcashCard getRfid() {
        return rfid;
    }

    public void setRfid(TapcashCard rfid) {
        this.rfid = rfid;
    }
}
