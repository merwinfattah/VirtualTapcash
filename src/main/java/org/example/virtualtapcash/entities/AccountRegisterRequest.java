package org.example.virtualtapcash.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegisterRequest {

    private String username;

    private String pin;

    private String customerName;

}
