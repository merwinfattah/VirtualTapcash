package org.example.virtualtapcash.dto.transaction.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private String cardId;
    private BigDecimal nominal;
    private String virtual_tapcash_id;
    private String type;
    private String pin;
}