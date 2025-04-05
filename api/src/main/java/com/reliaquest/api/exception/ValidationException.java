package com.reliaquest.api.exception;

import lombok.Getter;

@Getter
public class ValidationException  extends RuntimeException {
    private final CustomError error;

    public ValidationException(CustomError error) {
        this.error = error;
    }

}