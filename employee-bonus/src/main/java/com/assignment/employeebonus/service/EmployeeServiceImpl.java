package com.assignment.employeebonus.service;

import com.assignment.employeebonus.exceptions.EmployeeException;
import com.assignment.employeebonus.model.Department;
import com.assignment.employeebonus.model.Employee;
import com.assignment.employeebonus.repository.DepartmentRepository;
import com.assignment.employeebonus.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public void saveEmployees(Map<String, List<Map<String, Object>>> payload) throws EmployeeException {
        List<Map<String, Object>> employeeData = payload.get("employees");

        List<Employee> employees = employeeData.stream().map(data -> {
            Employee employee = new Employee();

            employee.setEmpName((String) data.get("empName"));
            employee.setAmount((Integer) data.get("amount"));
            employee.setCurrency((String) data.get("currency"));

            LocalDate joiningDate = parseDate((String) data.get("joiningDate"));
            LocalDate exitDate = parseDate((String) data.get("exitDate"));
            employee.setJoiningDate(joiningDate);
            employee.setExitDate(exitDate);

            String departmentName = (String) data.get("department");
            Department department = getOrCreateDepartment(departmentName);
            employee.setDepartment(department);

            return employee;
        }).collect(Collectors.toList());

        employeeRepository.saveAll(employees);
    }

    @Override
    public Map<String, Object> getEligibleEmployees(String dateString) throws EmployeeException {
        try {
            // Remove surrounding quotes from the date string if present
            dateString = dateString.replaceAll("^\"|\"$", "");

            LocalDate date = parseDate(dateString);
            List<Employee> allEmployees = employeeRepository.findAll();
            List<Employee> eligibleEmployees = allEmployees.stream()
                    .filter(emp -> (emp.getJoiningDate().isBefore(date) || emp.getJoiningDate().isEqual(date)) &&
                            (emp.getExitDate() == null || emp.getExitDate().isAfter(date) || emp.getExitDate().isEqual(date)))
                    .sorted(Comparator.comparing(Employee::getEmpName))
                    .collect(Collectors.toList());

            if (eligibleEmployees.isEmpty()) {
                throw new EmployeeException("No eligible employees found for the given date.");
            }

            Map<String, Object> response = new HashMap<>();
            eligibleEmployees.forEach(emp -> {
                String currency = emp.getCurrency();
                Map<String, Object> empData = new HashMap<>();
                empData.put("empName", emp.getEmpName());
                empData.put("amount", emp.getAmount());
                empData.put("department", emp.getDepartment().getName());

                if (response.containsKey(currency)) {
                    List<Map<String, Object>> empList = (List<Map<String, Object>>) response.get(currency);
                    empList.add(empData);
                } else {
                    List<Map<String, Object>> empList = new ArrayList<>();
                    empList.add(empData);
                    response.put(currency, empList);
                }
            });

            return response;
        } catch (DateTimeParseException e) {
            throw new EmployeeException("Invalid date format. Please use 'MMM-dd-yyyy' format for dates.");
        }
    }


    @Override
    @Transactional
    public Department getOrCreateDepartment(String departmentName) {
        Department department = departmentRepository.findByName(departmentName);
        if (department == null) {
            department = new Department(departmentName);
            department = departmentRepository.save(department);
        }
        return department;
    }

    private LocalDate parseDate(String dateString) throws DateTimeParseException {
        String[] parts = dateString.split("-");
        if (parts.length != 3) {
            throw new DateTimeParseException("Invalid date format. Please use 'MMM-dd-yyyy' format for dates.", dateString, 0);
        }

        String monthStr = parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1).toLowerCase();
        int dayOfMonth = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        Month month = mapMonth(monthStr);

        return LocalDate.of(year, month, dayOfMonth);
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
