package ru.tbank.practicum.exceptions;

public class NoSuchSettingFound extends RuntimeException {
    public NoSuchSettingFound(String message) {
        super(message);
    }
}
