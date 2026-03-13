package ru.tbank.practicum.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tbank.practicum.exceptions.HeaterNotFoundException;
import ru.tbank.practicum.repository.dot.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HeaterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleHeaterNotFound(HeaterNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
