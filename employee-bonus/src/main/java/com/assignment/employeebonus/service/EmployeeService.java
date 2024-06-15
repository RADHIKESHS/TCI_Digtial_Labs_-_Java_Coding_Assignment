package com.assignment.employeebonus.service;


import java.util.List;
import java.util.Map;

import com.assignment.employeebonus.exceptions.EmployeeException;
import com.assignment.employeebonus.model.Department;

public interface EmployeeService {
	void saveEmployees(Map<String, List<Map<String, Object>>> payload) throws EmployeeException;

	Map<String, Object> getEligibleEmployees(String dateString) throws EmployeeException;

	Department getOrCreateDepartment(String departmentName);
}