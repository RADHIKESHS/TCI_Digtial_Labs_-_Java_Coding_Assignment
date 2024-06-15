package com.assignment.employeebonus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String empName;
    private Integer amount;
    private String currency;
    @JsonFormat(pattern = "MMM-dd-yyyy")
    private LocalDate joiningDate;

    @JsonFormat(pattern = "MMM-dd-yyyy")
    private LocalDate exitDate;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
