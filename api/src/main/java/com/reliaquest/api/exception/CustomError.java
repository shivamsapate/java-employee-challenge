package com.reliaquest.api.exception;

import lombok.Getter;

@Getter
public enum CustomError {
    NULL_EMPLOYEE_ID("E1", "Employee ID cannot be null"),
    EMPLOYEE_NOT_FOUNT_BY_ID("E2", "Employee not found with the specified ID"),
    NO_EMPLOYEES_FOUND("E3", "No data available for employees"),
    MISSING_OR_INVALID_NAME("E4", "Name is required"),
    EMPLOYEE_NOT_FOUND_BY_NAME("E5", "Employee not found with the specified name"),
    MISSING_OR_INVALID_SALARY("E6", "Salary is required"),
    NEGATIVE_SALARY_NOT_ALLOWED("E7", "Salary cannot be a negative value"),
    MISSING_TITLE("E8", "Title is required"),
    MISSING_OR_INVALID_AGE("E9", "Age is required"),
    NEGATIVE_AGE_NOT_ALLOWED("E10", "Age cannot be negative"),
    INVALID_AGE_LIMIT("E11", "Age must be greater than 15 and less than 76"),
    REST_API_CALL_FAILURE("E12", "Failed to make the REST API request"),
    EMPLOYEE_API_RATE_LIMIT_EXCEEDED("E13", "Employee api rate limit exceeded");



    private final String code;
    private final String message;

    CustomError(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
