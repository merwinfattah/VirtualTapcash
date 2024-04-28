package org.example.virtualtapcash.exception.card;

public class CardNotFoundException extends RuntimeException{
    public CardNotFoundException(String message) {
        super(message);
    }
}
