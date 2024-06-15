# TCI_Digtial_Labs_-_Java_Coding_Assignment

# Employee Bonus Service

## Overview

The Employee Bonus Service is a Spring Boot application designed to manage employee data and calculate eligibility for bonuses. It allows for the saving of employee details and querying of eligible employees based on their joining and exit dates. The service organizes the eligible employees by currency, simplifying bonus distribution processes.

## Features

- **Save Employee Data**: Accepts a list of employees with details including name, joining date, exit date, department, and salary information.
- **Query Eligible Employees**: Retrieves employees eligible for bonuses on a given date and organizes them by currency.
- **Automatic Department Management**: Automatically creates new departments if they don't exist in the system during employee data saving.

## Technologies Used

- **Java 17**
- **Spring Boot 2.6.x**
- **Spring Data JPA**
- **H2 Database** (for in-memory database management during development)
- **Maven** (for project build and dependency management)

## Project Structure

src
├── main
│   ├── java
│   │   └── com
│   │       └── assignment
│   │           └── employeebonus
│   │               ├── controller
│   │               │   └── EmployeeController.java
│   │               ├── exceptions
│   │               │   └── EmployeeException.java
│   │               ├── model
│   │               │   ├── Department.java
│   │               │   └── Employee.java
│   │               ├── repository
│   │               │   ├── DepartmentRepository.java
│   │               │   └── EmployeeRepository.java
│   │               └── service
│   │                   ├── EmployeeService.java
│   │                   └── EmployeeServiceImpl.java
│   └── resources
│       ├── application.properties
│       └── data.sql
└── test
    └── java
        └── com
            └── assignment
                └── employeebonus
                    └── EmployeeBonusServiceApplicationTests.java



## Key Components

- **EmployeeController.java**: Handles HTTP requests related to employees, including saving new employees and fetching eligible ones.
- **EmployeeService.java**: Interface defining the methods for employee operations.
- **EmployeeServiceImpl.java**: Implementation of the EmployeeService interface. Contains logic for saving employees, fetching eligible employees, and managing departments.
- **Employee.java**: Entity representing an employee in the system.
- **Department.java**: Entity representing a department in the system.
- **EmployeeRepository.java**: Repository interface for Employee entity, extending JpaRepository.
- **DepartmentRepository.java**: Repository interface for Department entity, extending JpaRepository.
- **EmployeeException.java**: Custom exception class for handling employee-related errors.

## Endpoints

### Save Employees

- **URL**: `/tci/employee-bonus`
- **Method**: POST
- **Request Body**: A JSON payload containing a list of employee data.

```json
{
  "employees": [
    {
      "empName": "John Doe",
      "joiningDate": "Jan-10-2021",
      "exitDate": "Dec-15-2023",
      "amount": 5000,
      "currency": "USD",
      "department": "Engineering"
    },
    {
      "empName": "Jane Smith",
      "joiningDate": "Feb-15-2022",
      "exitDate": null,
      "amount": 6000,
      "currency": "EUR",
      "department": "Marketing"
    }
  ]
}
```

### Get Employee for bonus

- **URL**: `/tci/employee-bonus`
- **Method**: GET
- **Query Parameter**: `date` - The date to check employee eligibility (format: MMM-dd-yyyy).
- **Example**: `/tci/employee-bonus?date=May-27-2022`
- **Response**:
  - **200 OK**: Returns a map of eligible employees organized by currency.
  - **400 Bad Request**: Invalid date format or no eligible employees found.
  - **500 Internal Server Error**: An unexpected error occurred.

## How to Run the Application

### Prerequisites

- Java 17 or later
- Maven 3.8 or later

### Steps

1. Clone the repository:

   ```bash
   git clone https://github.com/your-repo/employee-bonus-service.git
```
