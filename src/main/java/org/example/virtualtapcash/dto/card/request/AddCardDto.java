package org.example.virtualtapcash.dto.card.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCardDto {
    private String cardId;
    private String virtualTapcashId;
}
