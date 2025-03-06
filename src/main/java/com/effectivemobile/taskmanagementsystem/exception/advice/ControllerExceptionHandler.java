package com.effectivemobile.taskmanagementsystem.exception.advice;

import com.effectivemobile.taskmanagementsystem.exception.ErrorResponse;
import com.effectivemobile.taskmanagementsystem.exception.CustomException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Hidden
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        String causeMessage = e.getCause() != null ? e.getCause().getMessage() : "No cause available";
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage(), causeMessage, null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = "Validation failed: " + Objects.requireNonNull(e.getBindingResult().getFieldError())
                .getDefaultMessage();
        return ResponseEntity
                .status(e.getStatusCode())
                .body(new ErrorResponse(errorMessage, null, null));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleResponseException(CustomException e) {
        String causeMessage = e.getCause() != null ? e.getCause().getMessage() : "No cause available";
        return ResponseEntity
                .status(e.getResponseStatus())
                .body(new ErrorResponse(e.getMessage(), causeMessage, e.getSourceMethod()));
    }
}
