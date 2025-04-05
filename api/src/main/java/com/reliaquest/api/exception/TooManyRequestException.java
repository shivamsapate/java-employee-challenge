package com.reliaquest.api.exception;

public class TooManyRequestException extends RuntimeException{
    private final CustomError error;

    public TooManyRequestException(CustomError error){
        super(error.getMessage());
        this.error = error;
    }

    public CustomError getError() {
        return error;
    }
}
