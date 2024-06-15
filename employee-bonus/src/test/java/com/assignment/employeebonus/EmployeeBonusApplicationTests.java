package com.assignment.employeebonus;

import com.assignment.employeebonus.controller.EmployeeController;
import com.assignment.employeebonus.exceptions.EmployeeException;
import com.assignment.employeebonus.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class EmployeeBonusApplicationTests {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveEmployees_Success() throws EmployeeException {
        Map<String, Object> empData = Map.of(
                "empName", "John Doe",
                "amount", 1000,
                "currency", "USD",
                "joiningDate", "Jun-01-2020",
                "department", "IT",
                "exitDate", ""
        );
        Map<String, List<Map<String, Object>>> payload = Collections.singletonMap("employees", Collections.singletonList(empData));

        when(employeeService.saveEmployees(payload)).thenReturn(Collections.emptyMap());

        ResponseEntity<Map<String, Object>> response = employeeController.saveEmployees(payload);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyMap(), response.getBody());

        verify(employeeService, times(1)).saveEmployees(payload);
    }

    @Test
    public void testSaveEmployees_EmployeeException() throws EmployeeException {
        Map<String, Object> empData = Map.of(
                "empName", "John Doe",
                "amount", 1000,
                "currency", "USD",
                "joiningDate", "Jun-01-2020",
                "department", "IT",
                "exitDate", ""
        );
        Map<String, List<Map<String, Object>>> payload = Collections.singletonMap("employees", Collections.singletonList(empData));

        String errorMessage = "Invalid employee data";
        when(employeeService.saveEmployees(payload)).thenThrow(new EmployeeException(errorMessage));

        ResponseEntity<Map<String, Object>> response = employeeController.saveEmployees(payload);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Collections.singletonMap("errorMessage", errorMessage), response.getBody());

        verify(employeeService, times(1)).saveEmployees(payload);
    }

    @Test
    public void testSaveEmployees_GenericException() throws EmployeeException {
        Map<String, Object> empData = Map.of(
                "empName", "John Doe",
                "amount", 1000,
                "currency", "USD",
                "joiningDate", "Jun-01-2020",
                "department", "IT",
                "exitDate", ""
        );
        Map<String, List<Map<String, Object>>> payload = Collections.singletonMap("employees", Collections.singletonList(empData));

        String errorMessage = "Internal server error";
        when(employeeService.saveEmployees(payload)).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<Map<String, Object>> response = employeeController.saveEmployees(payload);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Collections.singletonMap("errorMessage", "An error occurred while saving employees: " + errorMessage), response.getBody());

        verify(employeeService, times(1)).saveEmployees(payload);
    }

    @Test
    public void testGetEligibleEmployees_Success() throws EmployeeException {
        String date = "Jun-15-2024";
        Map<String, Object> mockResponse = Map.of("USD", Arrays.asList(
                Map.of("empName", "John Doe", "amount", 2500, "department", "Operations"),
                Map.of("empName", "Jane Smith", "amount", 3000, "department", "Finance")
        ));

        when(employeeService.getEligibleEmployees(eq(date))).thenReturn(mockResponse);

        ResponseEntity<Map<String, Object>> response = employeeController.getEligibleEmployees(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());

        verify(employeeService, times(1)).getEligibleEmployees(eq(date));
    }

    @Test
    public void testGetEligibleEmployees_EmployeeException() throws EmployeeException {
        String date = "Jun-15-2024";
        String errorMessage = "Invalid date";

        when(employeeService.getEligibleEmployees(eq(date))).thenThrow(new EmployeeException(errorMessage));

        ResponseEntity<Map<String, Object>> response = employeeController.getEligibleEmployees(date);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(Collections.singletonMap("errorMessage", errorMessage), response.getBody());

        verify(employeeService, times(1)).getEligibleEmployees(eq(date));
    }

    @Test
    public void testGetEligibleEmployees_GenericException() throws EmployeeException {
        String date = "Jun-15-2024";
        String errorMessage = "Internal server error";

        when(employeeService.getEligibleEmployees(eq(date))).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<Map<String, Object>> response = employeeController.getEligibleEmployees(date);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Collections.singletonMap("errorMessage", "An error occurred while retrieving eligible employees: " + errorMessage), response.getBody());

        verify(employeeService, times(1)).getEligibleEmployees(eq(date));
    }
}
