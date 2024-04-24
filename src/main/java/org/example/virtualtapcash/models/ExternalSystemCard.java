package org.example.virtualtapcash.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tb_external_system_card")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalSystemCard {

    @Id
    private String cardId;

    @Column(name = "tapcash_balance")
    private BigDecimal tapCashBalance;

    @Column(name = "rfid")
    private String rfid;


}