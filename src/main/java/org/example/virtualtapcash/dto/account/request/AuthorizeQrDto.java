package org.example.virtualtapcash.dto.account.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizeQrDto {
    private Long userId;
    private String pin;
}
