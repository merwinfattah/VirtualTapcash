package org.example.virtualtapcash.exception.transaction;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String message) {
        super(message);
    }
}
