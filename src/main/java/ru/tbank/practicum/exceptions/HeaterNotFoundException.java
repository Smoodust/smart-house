package ru.tbank.practicum.exceptions;

public class HeaterNotFoundException extends RuntimeException {
    public HeaterNotFoundException(String message) {
        super(message);
    }
}
