package com.reliaquest.api.controller;

import com.reliaquest.api.models.Employee;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.DeleteEmployeeResponse;
import com.reliaquest.api.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {
    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee("123", "Shivam Sapate","50000", "25", "shivamsapate@gmail.com", "Software Engineer");
    }

    @Test
    void getAllEmployees_ShouldReturnEmployeeList() {
        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));
        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    void getEmployeesByNameSearch_ShouldReturnMatchingEmployees() {
        when(employeeService.getEmployeesByNameSearch("Shivam")).thenReturn(List.of(employee));
        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByNameSearch("Shivam");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Shivam Sapate", response.getBody().get(0).getName());
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee() {
        when(employeeService.getEmployeeById("123")).thenReturn(employee);
        ResponseEntity<Employee> response = employeeController.getEmployeeById("123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Shivam Sapate", response.getBody().getName());
    }

    @Test
    void getHighestSalaryOfEmployees_ShouldReturnSalary() {
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(50000);
        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(50000, response.getBody());
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_ShouldReturnTopTenNames() {
        Employee employee2 = new Employee("2", "John Doe", "5000", "25", "Software Engineer", "johndoe@gmail.com");

        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(Arrays.asList(employee,employee2));
        ResponseEntity<List<Employee>> response = employeeController.getTopTenHighestEarningEmployeeNames();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void createEmployee_ShouldReturnCreatedEmployee() {
        when(employeeService.submitEmployee(any())).thenReturn(employee);
        ResponseEntity<Employee> response = employeeController.createEmployee(new EmployeeRequest());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Shivam Sapate", response.getBody().getName());
    }

    @Test
    void deleteEmployeeById_ShouldReturnSuccessResponse() {
        DeleteEmployeeResponse deleteResponse = new DeleteEmployeeResponse();
        deleteResponse.setData(true);
        deleteResponse.setStatus("Success");

        when(employeeService.deleteEmployeeById("1")).thenReturn(deleteResponse);
        ResponseEntity<DeleteEmployeeResponse> response = employeeController.deleteEmployeeById("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deleteResponse, response.getBody());
    }


}