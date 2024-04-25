package org.example.virtualtapcash.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCardRequest {
    private String cardId;
    private String virtualTapcashId;
}
