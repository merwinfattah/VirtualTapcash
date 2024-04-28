package org.example.virtualtapcash.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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