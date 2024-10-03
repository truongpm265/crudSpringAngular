package com.example.searchdq.repository;

import com.example.searchdq.entity.Employee;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
@Transactional
public interface EmployeeDAO extends CrudRepository<Employee,Long>,JpaSpecificationExecutor<Employee>{

}