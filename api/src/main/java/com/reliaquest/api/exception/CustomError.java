package com.reliaquest.api.exception;

import lombok.Getter;

@Getter
public enum CustomError {
    NULL_EMPLOYEE_ID("ERR-001", "Employee ID cannot be null"),
    EMPLOYEE_NOT_FOUNT_BY_ID("ERR-002", "Employee not found with the specified ID"),
    NO_EMPLOYEES_FOUND("ERR-003", "No data available for employees"),
    MISSING_OR_INVALID_NAME("ERR-004", "Name is either invalid or empty"),
    EMPLOYEE_NOT_FOUND_BY_NAME("ERR-005", "Employee not found with the specified name"),
    MISSING_OR_INVALID_SALARY("ERR-006", "Salary either invalid or empty"),
    NEGATIVE_SALARY_NOT_ALLOWED("ERR-007", "Salary cannot be a negative value"),
    MISSING_OR_INVALID_TITLE("ERR-008", "Title is either invalid or empty"),
    MISSING_OR_INVALID_AGE("ERR-009", "Age is either invalid or empty"),
    NEGATIVE_AGE_NOT_ALLOWED("ERR-010", "Age cannot be negative"),
    INVALID_AGE_LIMIT("ERR-011", "Age must be greater than 15 and less than 76"),
    REST_API_CALL_FAILURE("ERR-012", "Unable to process the request at this time. Please try again later"),
    EMPLOYEE_API_RATE_LIMIT_EXCEEDED("ERR-013", "Employee api rate limit exceeded");

    private final String code;
    private final String message;

    CustomError(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
