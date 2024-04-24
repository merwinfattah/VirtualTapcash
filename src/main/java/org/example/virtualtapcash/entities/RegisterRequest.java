package org.example.virtualtapcash.entities;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String pin;
}
