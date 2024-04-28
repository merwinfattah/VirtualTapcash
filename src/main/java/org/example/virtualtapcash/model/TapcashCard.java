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
@Table(name = "tb_tapcash_card",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"rfid"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TapcashCard {

    @Id
    private String cardId;

    @Column(name = "rfid")
    private String rfid;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "tapcash_balance")
    private BigDecimal tapCashBalance;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "registered_at")
    private Date registeredAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "virtual_tapcash_id", referencedColumnName = "virtual_tapcash_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MBankingAccount user;
}
