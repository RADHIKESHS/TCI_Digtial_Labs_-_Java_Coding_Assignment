package com.assignment.employeebonus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String empName;
    private double amount;
    private String currency;
    private LocalDate joiningDate;
    private LocalDate exitDate;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}

