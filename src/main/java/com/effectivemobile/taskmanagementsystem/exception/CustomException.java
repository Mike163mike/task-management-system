package com.effectivemobile.taskmanagementsystem.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomException extends RuntimeException {

    private HttpStatus responseStatus;

    private final String sourceClazz;

    private final String sourceMethod;

    public CustomException(String message, Class<?> sourceClazz, String sourceMethod) {
        super(message);
        this.sourceClazz = sourceClazz != null ? sourceClazz.getSimpleName() : "null";
        this.sourceMethod = sourceMethod;
    }

    public CustomException(String message, HttpStatus responseStatus, Class<?> sourceClazz, String sourceMethod) {
        super(message);
        this.responseStatus = responseStatus;
        this.sourceClazz = sourceClazz != null ? sourceClazz.getSimpleName() : "null";
        this.sourceMethod = sourceMethod;
    }
}
