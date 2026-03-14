package ru.tbank.practicum.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tbank.practicum.exceptions.DeviceNotFoundException;
import ru.tbank.practicum.exceptions.NoSuchSettingFound;
import ru.tbank.practicum.repository.dot.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DeviceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDeviceNotFound(DeviceNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(NoSuchSettingFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleSettingNotFound(DeviceNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
