package org.example.virtualtapcash.dto.transaction.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrResponseDto {
    private long qrId;
    private LocalDateTime activationTime;
    private Boolean isActive;
    private String cardId;
}
