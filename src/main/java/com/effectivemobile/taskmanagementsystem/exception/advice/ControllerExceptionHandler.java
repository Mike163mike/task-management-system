package com.effectivemobile.taskmanagementsystem.exception.advice;

import com.effectivemobile.taskmanagementsystem.exception.ErrorResponse;
import com.effectivemobile.taskmanagementsystem.exception.ResponseException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Hidden
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = "Validation failed: " + Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return null;
//        return ResponseEntity
//                .status(e.getStatusCode());
//                .body(new ExceptionResponse(errorMessage, e.getCause().getMessage(),
//                        WeatherReportController.class.getSimpleName()));
    }

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ErrorResponse> handleResponseException(ResponseException e) {
        return ResponseEntity
                .status(e.getResponseStatus())
                .body(new ErrorResponse(e.getMessage(), e.getCause().getMessage(), e.getSourceMethod()));
    }
}
