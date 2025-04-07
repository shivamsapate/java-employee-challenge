package com.reliaquest.api.external;

import com.reliaquest.api.exception.CustomError;
import com.reliaquest.api.exception.CustomException;
import com.reliaquest.api.exception.TooManyRequestException;
import com.reliaquest.api.exception.ValidationException;
import com.reliaquest.api.feignClient.EmployeeClient;
import com.reliaquest.api.models.Employee;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.DeleteEmployeeResponse;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.response.GetEmployeeResponse;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Component
public class EmployeeAPIs {

    private final EmployeeClient employeeClient;

    public List<Employee> getAllEmployees() {
        try {
            log.info("Fetching all employees...");
            EmployeeResponse response = employeeClient.getAllEmployees();
            log.info("Successfully fetched employees: {}", response);
            return response.getData();
        } catch (FeignException.TooManyRequests e) {
            log.error("Error fetching employees: {}", e.getMessage(), e);
            throw new TooManyRequestException(CustomError.EMPLOYEE_API_RATE_LIMIT_EXCEEDED);
        } catch (FeignException e) {
            log.error("Error fetching employees: {}", e.getMessage(), e);
            throw new CustomException(CustomError.REST_API_CALL_FAILURE);
        }
    }

    public Employee getEmployeeById(String id) {
        try {
            log.info("Fetching employee with ID: {}", id);
            GetEmployeeResponse response = employeeClient.getEmployeeById(id);
            log.info("Successfully fetched employee: {}", response);

            return response.getData();
        } catch (FeignException.NotFound e) {
            log.error("Employee not found with ID: {}", id);
            throw new CustomException(CustomError.EMPLOYEE_NOT_FOUNT_BY_ID); // Add this enum if not present
        } catch (FeignException.TooManyRequests e) {
            log.error("Error fetching employees: {}", e.getMessage(), e);
            throw new TooManyRequestException(CustomError.EMPLOYEE_API_RATE_LIMIT_EXCEEDED);
        } catch (FeignException e) {
            log.error("Error fetching employees: {}", e.getMessage(), e);
            throw new CustomException(CustomError.REST_API_CALL_FAILURE);
        }
    }

    public Employee submitEmployee(EmployeeRequest request) {
        try {
            log.info("Submitting employee data: {}", request);
            GetEmployeeResponse response = employeeClient.submitEmployee(request);
            log.info("Successfully submitted employee: {}", response);
            return response.getData();
        }  catch (FeignException e) {
            log.error("Error fetching employees: {}", e.getMessage(), e);
            throw new CustomException(CustomError.REST_API_CALL_FAILURE);
        }
    }

    public DeleteEmployeeResponse deleteEmployee(String id) {
        try {
            Employee employee = getEmployeeById(id);
            log.info("Deleting employee: {}", employee);

            EmployeeRequest request = new EmployeeRequest();
            request.setName(employee.getName());

            DeleteEmployeeResponse response = employeeClient.deleteEmployee(request);
            log.info("Successfully deleted employee: {}", response);

            return response;
        } catch (FeignException.TooManyRequests e) {
            log.error("Employee rate limit reached : {}", e.getMessage(), e);
            throw new TooManyRequestException(CustomError.EMPLOYEE_API_RATE_LIMIT_EXCEEDED);
        } catch (FeignException e) {
            log.error("Error fetching employees: {}", e.getMessage(), e);
            throw new CustomException(CustomError.REST_API_CALL_FAILURE);
        }
    }
}
