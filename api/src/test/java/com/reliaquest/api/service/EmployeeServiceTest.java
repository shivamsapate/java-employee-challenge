package com.reliaquest.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.exception.CustomException;
import com.reliaquest.api.exception.ValidationException;
import com.reliaquest.api.external.EmployeeAPIs;
import com.reliaquest.api.models.Employee;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.DeleteEmployeeResponse;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeAPIs employeeApis;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private Request request;


    @BeforeEach
    void setUp() {
        employee = new Employee("123","Shivam Sapate", "50000",  "25","Software Enginner", "shivamspate@gmail.com");

    }

    @Test
    void getAllEmployees_ShouldReturnEmployeeList(){
        when(employeeApis.getAllEmployees()).thenReturn(List.of(employee));
        List<Employee> allEmployees = employeeService.getAllEmployees();

        assertFalse(allEmployees.isEmpty());
        assertEquals("Shivam Sapate", allEmployees.get(0).getName());
    }

    @Test
    void getAllEmployees_ShouldThrowException_WhenNoEmployeesFound(){
        when(employeeApis.getAllEmployees()).thenReturn(Collections.emptyList());
        assertThrows(CustomException.class, employeeService::getAllEmployees);
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee() {
        when(employeeApis.getEmployeeById("1")).thenReturn(employee);
        Employee result = employeeService.getEmployeeById("1");
        assertNotNull(result);
        assertEquals("Shivam Sapate", result.getName());
    }

    @Test
    void getEmployeeById_ShouldThrowException_WhenIdIsNull() {
        assertThrows(CustomException.class, () -> employeeService.getEmployeeById(null));
    }

    @Test
    void getEmployeesByNameSearch_ShouldReturnMatchingEmployees() {
        when(employeeApis.getAllEmployees()).thenReturn(List.of(employee));
        List<Employee> result = employeeService.getEmployeesByNameSearch("Shivam");
        assertEquals(1, result.size());
    }

    @Test
    void getEmployeesByNameSearch_ShouldThrowException_WhenNoMatchFound() {
        when(employeeApis.getAllEmployees()).thenReturn(List.of(employee));
        assertThrows(CustomException.class, () -> employeeService.getEmployeesByNameSearch("xyz"));
    }

    @Test
    void getHighestSalaryOfEmployees_ShouldReturnHighestSalary() {
        when(employeeApis.getAllEmployees()).thenReturn(List.of(employee));
        assertEquals(50000, employeeService.getHighestSalaryOfEmployees());
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_ShouldReturnTopTen() {
        Employee employee2 = new Employee("2", "John Doe", "5000", "25", "Software Engineer", "johndoe@gmail.com");

        when(employeeApis.getAllEmployees()).thenReturn(Arrays.asList(employee, employee2));
        List<Employee> result = employeeService.getTopTenHighestEarningEmployeeNames();


        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(1).getName());
        assertEquals("Shivam Sapate", result.get(0).getName());
    }

    @Test
    void submitEmployee_ShouldConvertAndSubmitEmployee() {
        EmployeeRequest employeeRequest = new EmployeeRequest("Shivam Sapate", 50000, 25, "Software Engineer");

        when(objectMapper.convertValue(any(), eq(EmployeeRequest.class))).thenReturn(employeeRequest);
        when(employeeApis.submitEmployee(employeeRequest)).thenReturn(employee);

        Employee result = employeeService.submitEmployee(new Object());
        assertEquals("Shivam Sapate", result.getName());
        assertEquals("123", result.getId());
    }

    @Test
    void deleteEmployeeById_ShouldDeleteEmployee() {
        DeleteEmployeeResponse response = new DeleteEmployeeResponse();
        when(employeeApis.deleteEmployee("1")).thenReturn(response);
        assertEquals(response, employeeService.deleteEmployeeById("1"));
    }

    @Test
    void validateEmployeeRequest_ShouldThrowExceptionForInvalidAge() {
        EmployeeRequest employeeRequest = new EmployeeRequest("Shivam Sapate", 50000, -1, "Software Engineer");
        when(objectMapper.convertValue(any(), eq(EmployeeRequest.class))).thenReturn(employeeRequest);
        assertThrows(ValidationException.class, () -> employeeService.submitEmployee(employeeRequest));
    }

    @Test
    void validateEmployeeRequest_ShouldThrowExceptionForInvalidSalary() {
        EmployeeRequest employeeRequest = new EmployeeRequest("Shivam Sapate", -1, 30, "Software Engineer");
        when(objectMapper.convertValue(any(), eq(EmployeeRequest.class))).thenReturn(employeeRequest);
        assertThrows(ValidationException.class, () -> employeeService.submitEmployee(employeeRequest));
    }

    @Test
    void validateEmployeeRequest_ShouldThrowExceptionForInvalidName() {
        EmployeeRequest employeeRequest = new EmployeeRequest(null, 5000, 30, "Software Engineer");
        when(objectMapper.convertValue(any(), eq(EmployeeRequest.class))).thenReturn(employeeRequest);
        assertThrows(ValidationException.class, () -> employeeService.submitEmployee(employeeRequest));
    }

    @Test
    void validateEmployeeRequest_ShouldThrowExceptionForInvalidTitle() {
        EmployeeRequest employeeRequest = new EmployeeRequest("Shivam Sapate", -1, 30, null);
        when(objectMapper.convertValue(any(), eq(EmployeeRequest.class))).thenReturn(employeeRequest);
        assertThrows(ValidationException.class, () -> employeeService.submitEmployee(employeeRequest));
    }


}