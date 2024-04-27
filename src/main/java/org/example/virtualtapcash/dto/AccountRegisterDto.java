package org.example.virtualtapcash.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegisterDto {

    private String username;

    private String pin;

    private String customerName;

}
