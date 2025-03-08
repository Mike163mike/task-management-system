package com.effectivemobile.taskmanagementsystem.exception.advice;

import com.effectivemobile.taskmanagementsystem.exception.CustomException;
import com.effectivemobile.taskmanagementsystem.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestControllerAdvice
@Hidden
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected error occurred", e);
        HttpStatus status = resolveHttpStatus(e);
        return buildErrorResponse(status, e.getMessage(), e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        String errorMessage = "Validation failed: " + String.join("; ", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorMessage, null, null));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleResponseException(CustomException e) {
        return buildErrorResponse(e.getResponseStatus(), e.getMessage(), e);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, Exception e) {
        String causeMessage = Optional.ofNullable(e.getCause())
                .map(Throwable::getMessage)
                .orElse("No cause available");

        return ResponseEntity.status(status)
                .body(new ErrorResponse(message, causeMessage, null));
    }

    private HttpStatus resolveHttpStatus(Exception e) {
        if (e instanceof ResponseStatusException ex) {
            return (HttpStatus) ex.getStatusCode();
        }

        ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.value();
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
