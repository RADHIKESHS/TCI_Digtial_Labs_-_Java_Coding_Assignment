package com.assignment.employeebonus.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assignment.employeebonus.exceptions.EmployeeNotFoundException;
import com.assignment.employeebonus.exceptions.InvalidDateFormatException;
import com.assignment.employeebonus.model.Department;
import com.assignment.employeebonus.model.Employee;
import com.assignment.employeebonus.repository.DepartmentRepository;
import com.assignment.employeebonus.repository.EmployeeRepository;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;


    @Override
    @Transactional
    public void saveEmployees(List<Employee> employees) {
        for (Employee employee : employees) {
            employeeRepository.save(employee);
        }
    }

    @Override
    public Map<String, List<Employee>> getEligibleEmployees(LocalDate date) {
        LocalDate bonusDate=date;

        List<Employee> allEmployees = employeeRepository.findAll();
        List<Employee> eligibleEmployees = allEmployees.stream()
                .filter(emp -> (emp.getJoiningDate().isBefore(bonusDate) || emp.getJoiningDate().isEqual(bonusDate)) &&
                               (emp.getExitDate() == null || emp.getExitDate().isAfter(bonusDate) || emp.getExitDate().isEqual(bonusDate)))
                .sorted(Comparator.comparing(Employee::getEmpName))
                .collect(Collectors.toList());

        if (eligibleEmployees.isEmpty()) {
            throw new EmployeeNotFoundException("No eligible employees found for the given date.");
        }

        return eligibleEmployees.stream()
                .collect(Collectors.groupingBy(Employee::getCurrency));
    }
    
    


}