package org.example.virtualtapcash.exception.transaction;

public class ErrorTransaction extends RuntimeException {
    public ErrorTransaction(String message) {
        super(message);
    }
}
