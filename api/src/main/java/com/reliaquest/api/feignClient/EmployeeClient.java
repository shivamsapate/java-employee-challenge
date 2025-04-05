package com.reliaquest.api.feignClient;

import com.reliaquest.api.response.DeleteEmployeeResponse;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.response.GetEmployeeResponse;
import com.reliaquest.api.request.EmployeeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "employeeClient", url = "http://localhost:8112/api/v1/employee")
public interface EmployeeClient {

    @GetMapping
    EmployeeResponse getAllEmployees();

    @GetMapping("/{id}")
    GetEmployeeResponse getEmployeeById(@PathVariable("id") String id);

    @PostMapping
    GetEmployeeResponse submitEmployee(@RequestBody EmployeeRequest request);

    @DeleteMapping
    DeleteEmployeeResponse deleteEmployee(@RequestBody EmployeeRequest request);
}
