package org.example.virtualtapcash.dto.card.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveCardDto {
    private String cardId;
    private String pin;
}
