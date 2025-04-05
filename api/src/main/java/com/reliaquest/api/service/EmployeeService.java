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
import org.springframework.stereotype.Service;

import java.util.List;
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
            throw new CustomException(CustomError.NO_EMPLOYEES_FOUND);
        }

        log.info("List of Employees details {}", allEmployees);
        return allEmployees;
    }

    public Employee getEmployeeById(String id) {
        if(Objects.isNull(id)){
            throw new CustomException(CustomError.NULL_EMPLOYEE_ID);
        }
        Employee employee = employeeApis.getEmployeeById(id);
        if(Objects.isNull(employee)){
            throw new CustomException(CustomError.NO_EMPLOYEES_FOUND);
        }
        log.info("Employee details {}", employee);
        return employee;

    }

    public List<Employee> getEmployeesByNameSearch(String name) {
        List<Employee> employeesFoundByName = getAllEmployees().stream()
                .filter(employee -> employee.getName().contains(name))
                .collect(Collectors.toList());
        if(employeesFoundByName.isEmpty()) {
            throw new CustomException(CustomError.EMPLOYEE_NOT_FOUND_BY_NAME);
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
        EmployeeRequest employeeRequest = objectMapper.convertValue(employeeObject, EmployeeRequest.class);
        log.info("Submit employee request {}", employeeRequest);

        validateEmployeeRequest(employeeRequest);

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

    private void validateEmployeeRequest(EmployeeRequest request){
        log.info("validating employee request {}", request);

        if(Objects.isNull(request.getName()) || request.getName().isEmpty()){
            log.info("validation failed reason: {}", CustomError.MISSING_OR_INVALID_NAME.getMessage());
            throw new ValidationException(CustomError.MISSING_OR_INVALID_NAME);
        }

        if(Objects.isNull(request.getSalary())){
            log.info("validation failed reason: {}", CustomError.MISSING_OR_INVALID_SALARY.getMessage());
            throw new ValidationException(CustomError.MISSING_OR_INVALID_SALARY);
        }else if( request.getSalary() < 0){
            log.info("validation failed reason: {}", CustomError.NEGATIVE_SALARY_NOT_ALLOWED.getMessage());
            throw new ValidationException(CustomError.NEGATIVE_SALARY_NOT_ALLOWED);
        }

        if(Objects.isNull(request.getAge())){
            log.info("validation failed reason: {}", CustomError.MISSING_OR_INVALID_AGE.getMessage());
            throw new ValidationException(CustomError.MISSING_OR_INVALID_AGE);
        }else if( request.getAge() < 0){
            log.info("validation failed reason: {}", CustomError.NEGATIVE_AGE_NOT_ALLOWED.getMessage());
            throw new ValidationException(CustomError.NEGATIVE_AGE_NOT_ALLOWED);
        } else if(request.getAge() < 15 || request.getAge() > 76){
            log.info("validation failed reason: {}", CustomError.INVALID_AGE_LIMIT.getMessage());
            throw new ValidationException(CustomError.INVALID_AGE_LIMIT);
        }

        if(Objects.isNull(request.getTitle())){
            log.info("validation failed reason {}", CustomError.INVALID_AGE_LIMIT.getMessage());
            throw new ValidationException(CustomError.MISSING_TITLE);
        }
    }
}
