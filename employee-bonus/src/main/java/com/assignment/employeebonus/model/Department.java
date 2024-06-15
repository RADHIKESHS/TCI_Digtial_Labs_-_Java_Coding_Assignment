package com.assignment.employeebonus.model;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    

    public Department(String name) {
		super();
		this.name = name;
	}



	@OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Employee> employees;

}
