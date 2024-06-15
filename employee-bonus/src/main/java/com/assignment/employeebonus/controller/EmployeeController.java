package com.assignment.employeebonus.controller;

import com.assignment.employeebonus.exceptions.EmployeeException;
import com.assignment.employeebonus.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tci/employee-bonus")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<String> saveEmployees(@RequestBody Map<String, List<Map<String, Object>>> payload) {
        try {
            employeeService.saveEmployees(payload);
            return ResponseEntity.ok("Employees saved successfully.");
        } catch (EmployeeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving employees: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getEligibleEmployees(@RequestParam("date") String date) {
        try {
            Map<String, Object> response = employeeService.getEligibleEmployees(date);
            return ResponseEntity.ok(response);
        } catch (EmployeeException e) {
            return ResponseEntity.badRequest().body(Map.of("errorMessage", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("errorMessage", "An error occurred while retrieving eligible employees: " + e.getMessage()));
        }
    }

}

