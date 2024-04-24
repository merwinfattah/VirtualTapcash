package org.example.virtualtapcash.entities;


import lombok.Data;

@Data
public class AuthRequest {
    private String Username;
    private String pin;
}
