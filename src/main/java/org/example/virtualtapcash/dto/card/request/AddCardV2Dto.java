package org.example.virtualtapcash.dto.card.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCardV2Dto {
    private String rfid;
    private String virtualTapcashId;
}
