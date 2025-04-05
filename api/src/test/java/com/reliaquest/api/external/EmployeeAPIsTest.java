package com.reliaquest.api.external;

import com.reliaquest.api.exception.CustomError;
import com.reliaquest.api.exception.CustomException;
import com.reliaquest.api.exception.TooManyRequestException;
import com.reliaquest.api.feignClient.EmployeeClient;
import com.reliaquest.api.models.Employee;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.DeleteEmployeeResponse;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.response.GetEmployeeResponse;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeAPIsTest {

    @Mock
    private EmployeeClient employeeClient;

    @InjectMocks
    private EmployeeAPIs employeeAPIs;

    private Employee employee;
    private EmployeeRequest employeeRequest;
    private GetEmployeeResponse getEmployeeResponse;
    private DeleteEmployeeResponse deleteEmployeeResponse;
    private Request request;

    @BeforeEach
    void setUp() {
        employee = new Employee("1","John Doe","50000", "25", "Software Engineer","john.doe@example.com" );
        employeeRequest = new EmployeeRequest("John Doe",50000, 25, "Software Engineer");

        getEmployeeResponse = new GetEmployeeResponse();
        getEmployeeResponse.setData(employee);

        deleteEmployeeResponse = new DeleteEmployeeResponse();
        deleteEmployeeResponse.setData(true);

        request = Request.create(Request.HttpMethod.GET, "/api/v1/employee",
                Map.of(), null, new RequestTemplate());
    }

    @Test
    void testGetAllEmployees_Success() {
        when(employeeClient.getAllEmployees()).thenReturn(new EmployeeResponse("success",List.of(employee)));

        List<Employee> result = employeeAPIs.getAllEmployees();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void getAllEmployees_ShouldThrowTooManyRequestException() {
        FeignException exception = new FeignException.TooManyRequests("Too Many", request, null, null);
        when(employeeClient.getAllEmployees()).thenThrow(exception);

        TooManyRequestException ex = assertThrows(TooManyRequestException.class, () -> employeeAPIs.getAllEmployees());
        assertEquals(CustomError.EMPLOYEE_API_RATE_LIMIT_EXCEEDED, ex.getError());
    }

    @Test
    void getAllEmployees_ShouldThrowCustomException() {
        FeignException exception = new FeignException.BadRequest("Bad Request", request, null, null);
        when(employeeClient.getAllEmployees()).thenThrow(exception);

        CustomException ex = assertThrows(CustomException.class, () -> employeeAPIs.getAllEmployees());
        assertEquals(CustomError.REST_API_CALL_FAILURE, ex.getError());
    }

    @Test
    void testGetEmployeeById_Success() {
        when(employeeClient.getEmployeeById(anyString())).thenReturn(getEmployeeResponse);

        Employee result = employeeAPIs.getEmployeeById("123");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void getEmployeeById_ShouldThrowNotFound() {
        FeignException exception = new FeignException.NotFound("Not Found", request, null, null);
        when(employeeClient.getEmployeeById("1")).thenThrow(exception);

        CustomException ex = assertThrows(CustomException.class, () -> employeeAPIs.getEmployeeById("1"));
        assertEquals(CustomError.EMPLOYEE_NOT_FOUNT_BY_ID, ex.getError());
    }

    @Test
    void getEmployeeById_ShouldThrowTooManyRequest() {
        FeignException exception = new FeignException.TooManyRequests("Too Many", request, null, null);
        when(employeeClient.getEmployeeById("1")).thenThrow(exception);

        TooManyRequestException ex = assertThrows(TooManyRequestException.class, () -> employeeAPIs.getEmployeeById("1"));
        assertEquals(CustomError.EMPLOYEE_API_RATE_LIMIT_EXCEEDED, ex.getError());
    }

    @Test
    void testSubmitEmployee_Success() {
        when(employeeClient.submitEmployee(any(EmployeeRequest.class))).thenReturn(getEmployeeResponse);

        Employee result = employeeAPIs.submitEmployee(employeeRequest);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void submitEmployee_ShouldThrowCustomException() {
        FeignException exception = new FeignException.InternalServerError("Server Error", request, null, null);
        when(employeeClient.submitEmployee(any(EmployeeRequest.class))).thenThrow(exception);

        CustomException ex = assertThrows(CustomException.class, () -> employeeAPIs.submitEmployee(employeeRequest));
        assertEquals(CustomError.REST_API_CALL_FAILURE, ex.getError());
    }

    @Test
    void testDeleteEmployee_Success() {
        when(employeeClient.getEmployeeById(anyString())).thenReturn(getEmployeeResponse);
        when(employeeClient.deleteEmployee(any(EmployeeRequest.class))).thenReturn(deleteEmployeeResponse);

        DeleteEmployeeResponse result = employeeAPIs.deleteEmployee("123");

        assertNotNull(result);
        assertTrue(result.isData());
    }

    @Test
    void testDeleteEmployee_ShouldThrowTooManyRequestException() {
        request = Request.create(Request.HttpMethod.GET, "/api/v1/employee",
                Map.of(), null, new RequestTemplate());

        FeignException exception = new FeignException.TooManyRequests("Too Many", request, null, null);
        when(employeeClient.getEmployeeById(anyString())).thenThrow(exception);

        TooManyRequestException ex = assertThrows(TooManyRequestException.class, () -> employeeAPIs.deleteEmployee("1"));
        assertEquals(CustomError.EMPLOYEE_API_RATE_LIMIT_EXCEEDED, ex.getError());
    }

    @Test
    void testDeleteEmployee_ShouldThrowNotFoundException() {
        request = Request.create(Request.HttpMethod.GET, "/api/v1/employee",
                Map.of(), null, new RequestTemplate());

        FeignException exception = new FeignException.NotFound("Not found", request, null, null);
        when(employeeClient.getEmployeeById(anyString())).thenThrow(exception);

        CustomException ex = assertThrows(CustomException.class, () -> employeeAPIs.deleteEmployee("1"));
        assertEquals(CustomError.EMPLOYEE_NOT_FOUNT_BY_ID, ex.getError());
    }
}