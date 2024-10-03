package com.example.searchdq.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "employee") // Optional: Specify table name if different from class name
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "getAllEmployee", procedureName = "getAllEmployee"),
        @NamedStoredProcedureQuery(name="searchEmployee", procedureName = "searchEmployee")
})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String email;

    private LocalDate DOB;

    private String gender;

    private String location;
}