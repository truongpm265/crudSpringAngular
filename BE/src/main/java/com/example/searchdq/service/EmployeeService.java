package com.example.searchdq.service;

import com.example.searchdq.entity.Employee;
import com.example.searchdq.entity.EmployeeSearchRequest;
import com.example.searchdq.repository.EmployeeDAO;
import com.example.searchdq.repository.EmployeeEntityManager;
import com.example.searchdq.repository.EmployeeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeDAO employeeDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EmployeeEntityManager employeeEntityManager;

    public Page<Employee> searchEmployees(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findByNameContaining(name, pageable);
    }


    public Page<Employee> getEmployeesByDateRange(LocalDate fromDate, LocalDate toDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findEmployeesByDateBetween(fromDate, toDate, pageable);
    }

    public Page<Employee> searchEmployeeJson(String name, LocalDate fromDate, LocalDate toDate, String location, String gender, String email, int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        return employeeRepository.searchEmployees(name, fromDate, toDate, location, gender,email, pageable);
    }
    //---------------------------
    public Page<Employee> findByLikeCriteria(EmployeeSearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage() != null ? request.getPage() : 0,
                request.getSize() != null ? request.getSize() : 10);
        Page<Employee> page = employeeDAO.findAll(new Specification<Employee>() {
            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (request != null) {
                    if (request.getName() != null && !request.getName().trim().isEmpty()) {
                        String name = "%" + request.getName().trim().toLowerCase() + "%";
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), name));
                    }
                    if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                        String email = request.getEmail().trim().toLowerCase();
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), email));
                    }
                    if (request.getGender() != null && !request.getGender().trim().isEmpty()) {
                        String gender =request.getGender().trim().toLowerCase();
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("gender")), gender));
                    }
                    if (request.getLocation() != null && !request.getLocation().trim().isEmpty()) {
                        String location = "%" + request.getLocation().trim().toLowerCase() + "%";
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), location));
                    }if(request.getFromDate()!= null ){
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("DOB"), request.getFromDate()));
                    }if(request.getToDate()!= null){
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("DOB"), request.getToDate()));
                    }
                }
                query.orderBy(criteriaBuilder.asc(root.get("name")));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        return page;
    }
    //----------------------
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(int id, Employee employeeDetails) throws Exception {
        // Tìm nhân viên theo ID
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isPresent()) {
            Employee existingEmployee = employeeOptional.get();

            // Cập nhật thông tin nhân viên
            existingEmployee.setName(employeeDetails.getName());
            existingEmployee.setEmail(employeeDetails.getEmail());
            existingEmployee.setDOB(employeeDetails.getDOB());
            existingEmployee.setGender(employeeDetails.getGender());
            existingEmployee.setLocation(employeeDetails.getLocation());

            // Lưu lại thông tin sau khi cập nhật
            return employeeRepository.save(existingEmployee);
        } else {
            throw new Exception("Employee not found with ID: " + id);
        }
    }

    public Employee addNewEmployee(Employee employee) {return this.employeeRepository.save(employee);}

    public List<Employee> getAllEmployee(){return this.employeeRepository.findAll();}

    public Employee getEmployeeById(Integer id){
        Optional<Employee> employeeOptional = this.employeeRepository.findById(id);
        if(employeeOptional.isPresent()){
            return employeeOptional.get();
        }
        return null;
    }

    public Employee updateEmployee(Employee employee){
        Employee currentEmployee = this.getEmployeeById(employee.getId());
        if(currentEmployee != null ){
            currentEmployee.setName(employee.getName());
            currentEmployee.setEmail(employee.getEmail());
            currentEmployee.setDOB(employee.getDOB());
            currentEmployee.setLocation(employee.getLocation());
            currentEmployee.setGender(employee.getGender());
        }
        return employeeRepository.save(currentEmployee);
    }

    public void deleteEmployee(Integer id) {
        this.employeeRepository.deleteById(id);
    }

//----------------Entity Manager

    public Employee addNewEmployeeEM(Employee employee) {return employeeEntityManager.save(employee);}

    public List<Employee> findAllEM(){
        List<Employee> employees = employeeEntityManager.findAll();
        return  employees;
    }

    public Employee findByIdEM(Integer id){
        return employeeEntityManager.findById(id);
    }

    public Employee updateEmployeeEM(Employee employee){
        Employee currentEmployee = employeeEntityManager.findById(employee.getId());
        if(currentEmployee != null ){
            currentEmployee.setName(employee.getName());
            currentEmployee.setEmail(employee.getEmail());
            currentEmployee.setDOB(employee.getDOB());
            currentEmployee.setLocation(employee.getLocation());
            currentEmployee.setGender(employee.getGender());
        }
        return employeeEntityManager.update(currentEmployee);
    }

    public void deleteEmployeeEM(Integer id) {
        employeeEntityManager.delete(id);
    }

    public List<Employee> getEmployeesByDateRangeEM(LocalDate fromDate, LocalDate toDate) {
        return  employeeEntityManager.searchEmployeesByParams(fromDate,toDate);
    }
    public Page<Employee> searchEmployeesEM(EmployeeSearchRequest request) {
        return employeeEntityManager.findByLikeCriteria(request);
    }

    public List<Employee> searchEmployeesByRequest(LocalDate fromDate, LocalDate toDate) {
        return employeeEntityManager.searchEmployeesByRequest(fromDate, toDate);
    }

    public List<Employee> searchEmployeesByJson(EmployeeSearchRequest request) {
        return employeeEntityManager.searchEmployeesByJson(request);
    }

    public Page<Employee> search(EmployeeSearchRequest request, Pageable pageable) {
        // Base SQL statements with 1=1 for easier condition appending
        StringBuilder sql = new StringBuilder("SELECT s FROM Employee s WHERE 1=1");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(s) FROM Employee s WHERE 1=1");
        StringBuilder where = new StringBuilder();

        // Map to hold parameter values for the query
        Map<String, Object> params = new HashMap<>();

        // Add conditions dynamically based on non-null and non-empty values
        if (request.getName() != null && !request.getName().isEmpty()) {
            where.append(" AND s.name LIKE :name");
            params.put("name", "%" + request.getName().trim().toLowerCase() + "%");
        }

        if (request.getGender() != null && !request.getGender().isEmpty()) {
            where.append(" AND s.gender LIKE :gender");
            params.put("gender", "%" + request.getGender().trim().toLowerCase() + "%");
        }

        if (request.getLocation() != null && !request.getLocation().isEmpty()) {
            where.append(" AND s.address LIKE :address");
            params.put("address", "%" + request.getLocation().trim().toLowerCase() + "%");
        }

        // Finalize SQL statements with the constructed WHERE clause
        sql.append(where);
        sqlCount.append(where);

        // Query execution (assuming EntityManager is used for the custom query)
        Query query = entityManager.createQuery(sql.toString(), Employee.class);
        Query countQuery = entityManager.createQuery(sqlCount.toString());

        // Set parameters to both main and count queries
        params.forEach((key, value) -> {
            query.setParameter(key, value);
            countQuery.setParameter(key, value);
        });

        // Pagination
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Execute and get results
        List<Employee> employees = query.getResultList();
        long total = (long) countQuery.getSingleResult();

        // Return paginated result
        return new PageImpl<>(employees, pageable, total);
    }
//-----------------Stored Procedures------------------------------------------------------------------
    public List getAllEmployeeBySP(){
    return entityManager.createNamedStoredProcedureQuery("getAllEmployee").getResultList();
    }

    public List<Employee> searchEmployeesBySP(String name, String gender, String location, String email) {
        return employeeRepository.searchEmployee(name, gender, location, email);
    }

}