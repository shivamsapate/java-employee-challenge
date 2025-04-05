package com.reliaquest.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Employee {
    @JsonProperty("id")
    private String id;

    @JsonProperty("employee_name")
    private String name;

    @JsonProperty("employee_salary")
    private String salary;

    @JsonProperty("employee_age")
    private String age;

    @JsonProperty("employee_title")
    private String title;

    @JsonProperty("employee_email")
    private String email;
}