package com.reliaquest.api.response;

import com.reliaquest.api.models.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeResponse {

    private String status;
    private List<Employee> data;

}
