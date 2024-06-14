package com.assignment.employeebonus.exceptions;

public class DepartmentNotFoundException extends EmployeeException {
    public DepartmentNotFoundException(String message) {
        super(message);
    }
}