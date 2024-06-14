package com.assignment.employeebonus.service;


import java.util.List;
import java.util.Map;

import com.assignment.employeebonus.model.Employee;

public interface EmployeeService {
	public void saveEmployees(List<Employee> employees);
    public Map<String, List<Employee>> getEligibleEmployees(String date);
}