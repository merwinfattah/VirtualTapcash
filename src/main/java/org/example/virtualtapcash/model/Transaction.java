package org.example.virtualtapcash.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "tb_transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;

    @Column(name = "type")
    private String type;

    @Column(name = "nominal")
    private BigDecimal nominal;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "virtual_tapcash_id", referencedColumnName = "virtual_tapcash_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MBankingAccount user;

    @ManyToOne
    @JoinColumn(name = "cardId", referencedColumnName = "cardId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TapcashCard card;
}