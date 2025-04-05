package com.reliaquest.api.controller;

import com.reliaquest.api.exception.CustomException;
import com.reliaquest.api.exception.TooManyRequestException;
import com.reliaquest.api.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class EmployeeControllerAdvice {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        log.info("ValidationException because {}", ex.getError().getMessage());
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getError().getCode());
        errorResponse.put("message", ex.getError().getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(TooManyRequestException.class)
    public ResponseEntity<Map<String, String>> handleTooManyRequestException(TooManyRequestException ex) {
        log.info("handleTooManyRequestException because {}", ex.getError().getMessage());
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getError().getCode());
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(CustomException ex) {
        log.info("CustomException because {}", ex.getError().getMessage());
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getError().getCode());
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        log.info("Exception because {}", ex.getMessage());
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
