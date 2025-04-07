package com.reliaquest.api.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private final CustomError error;
    private final HttpStatus statusCode;

    public CustomException(CustomError error, HttpStatus statusCode){
        super(error.getMessage());
        this.error = error;
        this.statusCode = statusCode;
    }

    public CustomError getError() {
        return error;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}