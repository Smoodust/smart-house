package ru.tbank.practicum.exception;

public class NoSuchSettingFound extends RuntimeException {
  public NoSuchSettingFound(String message) {
    super(message);
  }
}
