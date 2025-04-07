package com.reliaquest.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.exception.CustomError;
import com.reliaquest.api.exception.CustomException;
import com.reliaquest.api.exception.ValidationException;
import com.reliaquest.api.external.EmployeeAPIs;
import com.reliaquest.api.models.Employee;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.DeleteEmployeeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeAPIs employeeApis;

    private final ObjectMapper objectMapper;

    public List<Employee> getAllEmployees() {
        List<Employee> allEmployees = employeeApis.getAllEmployees();
        if(allEmployees.isEmpty()){
            throw new CustomException(CustomError.NO_EMPLOYEES_FOUND, HttpStatus.NOT_FOUND);
        }

        log.info("List of Employees details {}", allEmployees);
        return allEmployees;
    }

    public Employee getEmployeeById(String id) {
        if(Objects.isNull(id)){
            throw new CustomException(CustomError.NULL_EMPLOYEE_ID, HttpStatus.BAD_REQUEST);
        }
        Employee employee = employeeApis.getEmployeeById(id);
        if(Objects.isNull(employee)){
            throw new CustomException(CustomError.NO_EMPLOYEES_FOUND, HttpStatus.NOT_FOUND);
        }
        log.info("Employee details {}", employee);
        return employee;

    }

    public List<Employee> getEmployeesByNameSearch(String name) {
        List<Employee> employeesFoundByName = getAllEmployees().stream()
                .filter(employee -> employee.getName().contains(name))
                .collect(Collectors.toList());
        if(employeesFoundByName.isEmpty()) {
            throw new CustomException(CustomError.EMPLOYEE_NOT_FOUND_BY_NAME, HttpStatus.NOT_FOUND);
        }
        log.info("Employees with name {} are : {}", name, employeesFoundByName);
        return employeesFoundByName;
    }

    public int getHighestSalaryOfEmployees() {
        int highestSalaryOfEmployees = getAllEmployees().stream()
                .map(employee -> Integer.parseInt(employee.getSalary()))
                .reduce(Integer::max)
                .orElse(0);
        log.info("The highest salary among employees is {}", highestSalaryOfEmployees);
        return highestSalaryOfEmployees;
    }

    public List<Employee> getTopTenHighestEarningEmployeeNames() {
        List<Employee> topTenHighestEarningEmployees = getAllEmployees().stream()
                .sorted((e1, e2) -> Integer.parseInt(e2.getSalary()) - Integer.parseInt(e1.getSalary()))
                .limit(10)
                .collect(Collectors.toList());

        log.info("Top 10 highest earning employee names: {}", topTenHighestEarningEmployees);
        return topTenHighestEarningEmployees;
    }

    public Employee submitEmployee(Object employeeObject){
        Map<String, Object> employeeMap = (Map<String, Object>) employeeObject;
        log.info("Submit employee request {}", employeeMap);

        EmployeeRequest employeeRequest = validateEmployeeRequest(employeeMap);


        Employee employee = employeeApis.submitEmployee(employeeRequest);
        log.info("Details of newly created employee {}", employee);
        return employee;
    }

    public DeleteEmployeeResponse deleteEmployeeById(String id){
        log.info("Delete employee request with id {}", id);
        if(Objects.isNull(id)){
            throw new ValidationException(CustomError.NULL_EMPLOYEE_ID);
        }
        return employeeApis.deleteEmployee(id);
    }

    public EmployeeRequest validateEmployeeRequest(Map<String, Object> employeeInput) throws ValidationException {
        log.info("Validation started for create employee request body");

        if (!employeeInput.containsKey("name") || !(employeeInput.get("name") instanceof String) ||
                ((String) employeeInput.get("name")).isBlank()) {
            throw new ValidationException(CustomError.MISSING_OR_INVALID_NAME);
        }

        if (!employeeInput.containsKey("salary") || !(employeeInput.get("salary") instanceof Integer)) {
            throw new ValidationException(CustomError.MISSING_OR_INVALID_SALARY);
        }

        if ((Integer) employeeInput.get("salary") < 0) {
            throw new ValidationException(CustomError.NEGATIVE_SALARY_NOT_ALLOWED);
        }

        if (!employeeInput.containsKey("age") || !(employeeInput.get("age") instanceof Integer)) {
            throw new ValidationException(CustomError.MISSING_OR_INVALID_AGE);
        }

        if ((Integer) employeeInput.get("age") < 16 || (Integer) employeeInput.get("age") > 75) {
            throw new ValidationException(CustomError.INVALID_AGE_LIMIT);
        }

        if (!employeeInput.containsKey("title") || !(employeeInput.get("title") instanceof String) ||
                ((String) employeeInput.get("title")).isBlank()) {
            throw new ValidationException(CustomError.MISSING_OR_INVALID_TITLE);
        }
        log.info("Validation successful for create employee input");

        return EmployeeRequest.builder()
                .name((String) employeeInput.get("name"))
                .age((Integer) employeeInput.get("age"))
                .salary((Integer) employeeInput.get("salary"))
                .title((String) employeeInput.get("title"))
                .build();
    }
}
