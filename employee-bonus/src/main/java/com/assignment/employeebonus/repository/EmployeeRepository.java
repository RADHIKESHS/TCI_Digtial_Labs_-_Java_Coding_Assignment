package com.assignment.employeebonus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.employeebonus.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
