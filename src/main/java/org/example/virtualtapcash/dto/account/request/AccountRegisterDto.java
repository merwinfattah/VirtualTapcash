package org.example.virtualtapcash.dto.account.request;

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
