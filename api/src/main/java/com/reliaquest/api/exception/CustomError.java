package com.reliaquest.api.exception;

import lombok.Getter;

@Getter
public enum CustomError {
    REST_API_CALL_FAILURE("E1", "Failed to make the REST API request"),
    JSON_PARSE_ERROR("E2", "Unable to parse the JSON response"),
    NO_EMPLOYEES_FOUND("E3", "No data available for employees"),
    EMPLOYEE_NOT_FOUND_BY_NAME("E4", "Employee not found with the specified name"),
    MISSING_OR_INVALID_NAME("E5", "Name is required"),
//    INVALID_FIELD_TYPE_FOR_NAME("E6","Name must be a string"),
//    INVALID_FIELD_TYPE_FOR_AGE("E6","Age must be an integer"),
//    INVALID_FIELD_TYPE_FOR_SALARY("E6","Salary must be an integer"),
//    INVALID_FIELD_TYPE_FOR_TITLE("E6","Title must be a string"),
    MISSING_OR_INVALID_SALARY("E6", "Salary is required"),
    NEGATIVE_SALARY_NOT_ALLOWED("E7", "Salary cannot be a negative value"),
    MISSING_OR_INVALID_AGE("E8", "Age is required"),
    NEGATIVE_AGE_NOT_ALLOWED("E9", "Age cannot be negative"),
    INVALID_AGE_LIMIT("E10", "Age must be greater than 15 and less than 76"),
    MISSING_TITLE("E11", "Title is required"),
    NULL_EMPLOYEE_ID("E12", "Employee ID cannot be null"),
    EMPLOYEE_NOT_FOUNT_BY_ID("E13", "Employee not found with the specified ID"),
    EMPLOYEE_API_RATE_LIMIT_EXCEEDED("E14", "Employee api rate limit exceeded");



    private final String code;
    private final String message;

    CustomError(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
