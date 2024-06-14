package com.assignment.employeebonus.controller;

import com.assignment.employeebonus.model.Employee;
import com.assignment.employeebonus.service.EmployeeService;
import com.assignment.employeebonus.exceptions.EmployeeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tci/employee-bonus")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<String> saveEmployees(@RequestBody Map<String, List<Employee>> payload) {
        try {
            List<Employee> employees = payload.get("employees");
            employeeService.saveEmployees(employees);
            return new ResponseEntity<>("Employees saved successfully.", HttpStatus.OK);
        } catch (EmployeeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while saving employees: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping
    public ResponseEntity<Map<String, Object>> getEligibleEmployees(@RequestParam("date") String date) {
        try {
            Map<String, List<Employee>> eligibleEmployees = employeeService.getEligibleEmployees(date);

            Map<String, Object> response = new HashMap<>();
            response.put("errorMessage", "");
            response.put("data", eligibleEmployees.entrySet().stream().map(entry -> {
                Map<String, Object> currencyData = new HashMap<>();
                currencyData.put("currency", entry.getKey());
                currencyData.put("employees", entry.getValue().stream().map(emp -> {
                    Map<String, Object> empData = new HashMap<>();
                    empData.put("empName", emp.getEmpName());
                    empData.put("amount", emp.getAmount());
                    return empData;
                }).collect(Collectors.toList()));
                return currencyData;
            }).collect(Collectors.toList()));

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EmployeeException e) {
            return new ResponseEntity<>(Map.of("errorMessage", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("errorMessage", "An error occurred while retrieving eligible employees: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
