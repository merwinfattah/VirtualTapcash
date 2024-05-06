package org.example.virtualtapcash.dto.card.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeCardDto {
    private String cardId;
    private Boolean is_default;
}