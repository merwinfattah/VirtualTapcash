package org.example.virtualtapcash.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table( name = "tb_tapcash_card")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TapcashCard {

    @Id
    private String cardId;

    @Column(name = "rfid")
    private String rfid;

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
    @JoinColumn(name = "virtual_tapcash_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MBankingAccount user;


}
