package com.example.searchdq.repository;

import com.example.searchdq.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {


    
    @Procedure(procedureName = "searchEmployee")
    List<Employee> searchEmployee(
            @Param("p_name") String name,
            @Param("p_gender") String gender,
            @Param("p_location") String location,
            @Param("p_email") String email
    );

    Page<Employee> findByNameContaining(String name, Pageable pageable);


    @Query("SELECT e FROM Employee e WHERE e.DOB BETWEEN :fromDate AND :toDate")
    Page<Employee> findEmployeesByDateBetween(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE " +
            "(:name IS NULL OR e.name LIKE %:name%) AND " +
            "(:fromDate IS NULL OR :toDate IS NULL OR e.DOB BETWEEN :fromDate AND :toDate) AND " +
            "(:location IS NULL OR e.location = :location) AND " +
            "(:gender IS NULL OR e.gender = :gender) AND" +
            "(:email IS NULL OR e.email = :email)")
    Page<Employee> searchEmployees(@Param("name") String name,
                                   @Param("fromDate") LocalDate fromDate,
                                   @Param("toDate") LocalDate toDate,
                                   @Param("location") String location,
                                   @Param("gender") String gender,
                                   @Param("email") String email,
                                   Pageable pageable);

    //--------------------------
}
