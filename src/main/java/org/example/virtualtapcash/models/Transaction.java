package org.example.virtualtapcash.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "tb_transaction")
public class Transaction {

    @Id
    private String transactionId;

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", type='" + type + '\'' +
                ", nominal=" + nominal +
                ", createdAt=" + createdAt +
                ", tapcash=" + tapcash +
                ", rfid=" + rfid +
                '}';
    }

    @Column(name = "type")
    private String type;

    @Column(name = "nominal")
    private BigDecimal nominal;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "virtual_tapcash_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TapcashCard tapcash;

    @ManyToOne
    @JoinColumn(name = "rfid")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TapcashCard rfid;

    public Transaction() {
    }

    public Transaction(String transactionId, String type, BigDecimal nominal, Date createdAt, TapcashCard tapcash, TapcashCard rfid) {
        this.transactionId = transactionId;
        this.type = type;
        this.nominal = nominal;
        this.createdAt = createdAt;
        this.tapcash = tapcash;
        this.rfid = rfid;
    }

    @NonNull
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull
    public BigDecimal getNominal() {
        return nominal;
    }

    public void setNominal(BigDecimal nominal) {
        this.nominal = nominal;
    }

    @NonNull
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @NonNull
    public TapcashCard getTapcash() {
        return tapcash;
    }

    public void setTapcash(TapcashCard tapcash) {
        this.tapcash = tapcash;
    }

    @NonNull
    public TapcashCard getRfid() {
        return rfid;
    }

    public void setRfid(TapcashCard rfid) {
        this.rfid = rfid;
    }
}
