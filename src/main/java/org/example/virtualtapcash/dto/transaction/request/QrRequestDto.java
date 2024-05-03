package org.example.virtualtapcash.dto.transaction.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrRequestDto {
    private String cardId;
}
