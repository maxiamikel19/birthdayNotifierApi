package com.maxiamikel19.birthday_notifier_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceDuplicatedException.class)
    public ResponseEntity<StandardError> handleResourceDuplicatedException(ResourceDuplicatedException ex) {
        return handleError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return handleError(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        if (ex.getMessage().contains("java.time.LocalDate")) {
            return handleError(HttpStatus.BAD_REQUEST, "The date value is incorrect. try like 'YYYY-MM-DD'");
        }
        return handleError(HttpStatus.BAD_REQUEST, "Only MALE or FEMALE value is accept");
    }

    @ExceptionHandler(InputValidationException.class)
    public ResponseEntity<StandardError> handleInputValidationException(InputValidationException ex) {
        return handleError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return handleError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    //@ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleException(Exception ex) {
        return handleError(HttpStatus.INTERNAL_SERVER_ERROR,
                "An error ocurred, please contact to the admin or try again later");
    }

    private ResponseEntity<StandardError> handleError(HttpStatus status, Object message) {
        StandardError error = new StandardError(
                status.value(),
                System.currentTimeMillis(),
                message);
        return ResponseEntity.status(status).body(error);
    }

}
