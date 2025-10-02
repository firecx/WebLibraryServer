package org.firecx.server.controllers;

import org.firecx.server.models.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionController {
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    private ExceptionResponse notFound(EntityNotFoundException ex) {
        return new ExceptionResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RuntimeException.class)
    private ExceptionResponse error(RuntimeException ex) {
        return new ExceptionResponse(ex.getMessage());
    }
}
