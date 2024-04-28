package org.example.virtualtapcash.dto.transaction.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResultDto {
    private boolean success;
    private String message;


}
