package com.assignment.employeebonus.controller;

import com.assignment.employeebonus.model.Department;
import com.assignment.employeebonus.model.Employee;
import com.assignment.employeebonus.repository.DepartmentRepository;
import com.assignment.employeebonus.service.EmployeeService;
import com.assignment.employeebonus.exceptions.EmployeeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tci/employee-bonus")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello! Welcome to my project", HttpStatus.ACCEPTED);
    }

    @PostMapping
    public ResponseEntity<String> saveEmployees(@RequestBody Map<String, List<Map<String, Object>>> payload) {
        try {
            List<Map<String, Object>> employeeData = payload.get("employees");

            List<Employee> employees = employeeData.stream().map(data -> {
                Employee employee = new Employee();

                employee.setEmpName((String) data.get("empName"));
                employee.setAmount(((Integer) data.get("amount"))); 
                employee.setCurrency((String) data.get("currency"));

                LocalDate joiningDate = parseDate((String) data.get("joiningDate"));
                LocalDate exitDate = parseDate((String) data.get("exitDate"));
                employee.setJoiningDate(joiningDate);
                employee.setExitDate(exitDate);
                
                String departmentName = (String) data.get("department");
                Department department = departmentRepository.findByName(departmentName);
                if (department == null) {
                    department = new Department(departmentName);
                    department = departmentRepository.save(department);
                }
                employee.setDepartment(department);

                return employee;
                
            }).collect(Collectors.toList());

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
        	
            date = date.replaceAll("^\"|\"$", "");
            LocalDate parsedDate = parseDate(date); 
            Map<String, List<Employee>> eligibleEmployees = employeeService.getEligibleEmployees(parsedDate);

            // Prepare the response
            Map<String, Object> response = eligibleEmployees.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream().map(emp -> {
                    Map<String, Object> empData = new HashMap<>();
                    empData.put("empName", emp.getEmpName());
                    empData.put("amount", emp.getAmount());
                    empData.put("department", emp.getDepartment().getName());
                    return empData;
                }).collect(Collectors.toList())
            ));

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(Map.of("errorMessage", "Invalid date format. Please use 'MMM-dd-yyyy' format."), HttpStatus.BAD_REQUEST);
        } catch (EmployeeException e) {
            return new ResponseEntity<>(Map.of("errorMessage", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("errorMessage", "An error occurred while retrieving eligible employees: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    private LocalDate parseDate(String dateString) throws DateTimeParseException, NumberFormatException {
        try {
            String[] parts = dateString.split("-");
            if (parts.length != 3) {
                throw new DateTimeParseException("Invalid date format. Please use 'MMM-dd-yyyy' format.", dateString, 0);
            }

            String monthStr = parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1).toLowerCase();
            int dayOfMonth = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            Month month = mapMonth(monthStr);

            return LocalDate.of(year, month, dayOfMonth);
        } catch (IllegalArgumentException e) {
            throw new DateTimeParseException("Invalid date format. Please use 'MMM-dd-yyyy' format.", dateString, 0);
        }
    }

    private Month mapMonth(String monthStr) {
        switch (monthStr) {
            case "Jan": return Month.JANUARY;
            case "Feb": return Month.FEBRUARY;
            case "Mar": return Month.MARCH;
            case "Apr": return Month.APRIL;
            case "May": return Month.MAY;
            case "Jun": return Month.JUNE;
            case "Jul": return Month.JULY;
            case "Aug": return Month.AUGUST;
            case "Sep": return Month.SEPTEMBER;
            case "Oct": return Month.OCTOBER;
            case "Nov": return Month.NOVEMBER;
            case "Dec": return Month.DECEMBER;
            default: throw new IllegalArgumentException("Invalid month value: " + monthStr);
        }
    }

}
