package com.reliaquest.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.exception.CustomError;
import com.reliaquest.api.exception.CustomException;
import com.reliaquest.api.exception.ValidationException;
import com.reliaquest.api.external.EmployeeAPIs;
import com.reliaquest.api.models.Employee;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.DeleteEmployeeResponse;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeAPIs employeeApis;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;


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
        Map<String, Object> employeeMap = Map.of("name", "Shivam Sapate", "salary", 50000, "age", 25, "title", "Engineer", "email", "shivam@company.com");
        EmployeeRequest employeeRequest = new EmployeeRequest("Shivam Sapate", 50000, 25, "Engineer");

        when(employeeApis.submitEmployee(employeeRequest)).thenReturn(employee);

        Employee result = employeeService.submitEmployee(employeeMap);
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
    void ValidateInput_Success() throws ValidationException {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Shivam Sapate");
        employeeInput.put("age", 40);
        employeeInput.put("salary", 70000);
        employeeInput.put("title", "Accountant");

        EmployeeRequest result = employeeService.validateEmployeeRequest(employeeInput);

        assertNotNull(result);
        assertEquals(70000, result.getSalary());
        assertEquals(40, result.getAge());
        assertEquals("Accountant", result.getTitle());
    }


    @Test
    void ValidateInput_MissingName() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("age", 40);
        employeeInput.put("salary", 70000);
        employeeInput.put("title", "Accountant");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            employeeService.validateEmployeeRequest(employeeInput);
        });

        assertEquals(CustomError.MISSING_OR_INVALID_NAME, exception.getError());
    }

    @Test
    void ValidateInput_BlankName() throws ValidationException {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "");
        employeeInput.put("age", 40);
        employeeInput.put("salary", 70000);
        employeeInput.put("title", "Accountant");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            employeeService.validateEmployeeRequest(employeeInput);
        });

        assertEquals(CustomError.MISSING_OR_INVALID_NAME, exception.getError());
    }

    @Test
    void ValidateInput_MissingAge() throws ValidationException {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Shivam Sapate");
        employeeInput.put("salary", 70000);
        employeeInput.put("title", "Accountant");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            employeeService.validateEmployeeRequest(employeeInput);
        });

        assertEquals(CustomError.MISSING_OR_INVALID_AGE, exception.getError());
    }

    @Test
    void ValidateInput_MinimumAge() throws ValidationException {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Shivam Sapate");
        employeeInput.put("age", 10);
        employeeInput.put("salary", 70000);
        employeeInput.put("title", "Accountant");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            employeeService.validateEmployeeRequest(employeeInput);
        });

        assertEquals(CustomError.INVALID_AGE_LIMIT, exception.getError());
    }

    @Test
    void ValidateInput_MaximumAge() throws ValidationException {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Shivam Sapate");
        employeeInput.put("age", 100);
        employeeInput.put("salary", 70000);
        employeeInput.put("title", "Accountant");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            employeeService.validateEmployeeRequest(employeeInput);
        });

        assertEquals(CustomError.INVALID_AGE_LIMIT, exception.getError());
    }

    @Test
    void ValidateInput_MissingSalary() throws ValidationException {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Shivam Sapate");
        employeeInput.put("age", 100);
        employeeInput.put("title", "Accountant");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            employeeService.validateEmployeeRequest(employeeInput);
        });

        assertEquals(CustomError.MISSING_OR_INVALID_SALARY, exception.getError());
    }

    @Test
    void ValidateInput_MinimumSalary() throws ValidationException {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Shivam Sapate");
        employeeInput.put("age", 100);
        employeeInput.put("salary", -200);
        employeeInput.put("title", "Accountant");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            employeeService.validateEmployeeRequest(employeeInput);
        });

        assertEquals(CustomError.NEGATIVE_SALARY_NOT_ALLOWED, exception.getError());
    }

    @Test
    void ValidateInput_MissingTitle() throws ValidationException {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Shivam Sapate");
        employeeInput.put("age", 30);
        employeeInput.put("salary", 200);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            employeeService.validateEmployeeRequest(employeeInput);
        });

        assertEquals(CustomError.MISSING_OR_INVALID_TITLE, exception.getError());
    }

    @Test
    void ValidateInput_BlankTitle() throws ValidationException {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Shivam Sapate");
        employeeInput.put("age", 30);
        employeeInput.put("salary", 2000);
        employeeInput.put("title", "");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            employeeService.validateEmployeeRequest(employeeInput);
        });

        assertEquals(CustomError.MISSING_OR_INVALID_TITLE, exception.getError());
    }

}