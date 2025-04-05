package com.reliaquest.api.controller;

import com.reliaquest.api.models.Employee;
import com.reliaquest.api.response.DeleteEmployeeResponse;
import com.reliaquest.api.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/employee")
@AllArgsConstructor
public class EmployeeController implements IEmployeeController{

    private final EmployeeService employeeService;

    @Override
    @GetMapping()
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("called api to get all employees details");
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Override
    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        log.info("called api to get all employees with name {}", searchString);
        return ResponseEntity.ok(employeeService.getEmployeesByNameSearch(searchString));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(String id) {
        log.info("called api to fetch employee details by id {}",id);
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        log.info("called api to get max salaried employee");
        return ResponseEntity.ok(employeeService.getHighestSalaryOfEmployees());
    }

    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<Employee>> getTopTenHighestEarningEmployeeNames() {
        log.info("called api to fetch top 10 earning employees name ");
        return ResponseEntity.ok(employeeService.getTopTenHighestEarningEmployeeNames());
    }

    @Override
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Object employeeInput) {
        log.info("called api to submit employee details");
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.submitEmployee(employeeInput));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteEmployeeResponse> deleteEmployeeById(@PathVariable String id) {
        log.info("called api to delete employee details");
        return ResponseEntity.ok(employeeService.deleteEmployeeById(id));
    }
}
