package org.example.virtualtapcash.exceptions;

public class ErrorTransaction extends RuntimeException {
    public ErrorTransaction(String message) {
        super(message);
    }
}
