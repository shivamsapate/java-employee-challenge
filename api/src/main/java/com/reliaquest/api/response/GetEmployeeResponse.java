package com.reliaquest.api.response;

import com.reliaquest.api.models.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEmployeeResponse {
    private String status;
    private Employee data;
}
