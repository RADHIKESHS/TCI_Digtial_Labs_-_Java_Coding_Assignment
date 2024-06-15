package com.assignment.employeebonus.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.employeebonus.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Employee findByEmpNameAndJoiningDateAndDepartment_Name(String empName, LocalDate joiningDate,
			String departmentName);

}
