package org.example.virtualtapcash.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private String rfid;
    private BigDecimal nominal;
    private String virtual_tapcash_id;
    private String type;
    private String pin;

}