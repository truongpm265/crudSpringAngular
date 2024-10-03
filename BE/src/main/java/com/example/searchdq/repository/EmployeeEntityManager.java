package com.example.searchdq.repository;

import com.example.searchdq.entity.Employee;
import com.example.searchdq.entity.EmployeeSearchRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class EmployeeEntityManager {
    @PersistenceContext
    private EntityManager entityManager;


    public Employee save(Employee employee) {
        entityManager.persist(employee);
        return employee;
    }

    public Employee update(Employee employee) {
        return entityManager.merge(employee);
    }

    public List<Employee> findAll() {
        String jpql = "SELECT c FROM Employee c";
        TypedQuery<Employee> query = entityManager.createQuery(jpql, Employee.class);

        return query.getResultList();
    }

    public Employee findById(Integer id){
        return entityManager.find(Employee.class,id);
    }

    public void delete(Integer id) {
        Employee employee = entityManager.find(Employee.class, id);
        entityManager.remove(employee);
    }

    public List<Employee> searchEmployeesByParams(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate) {
        String jpql = "SELECT e FROM Employee e WHERE e.DOB BETWEEN :fromDate AND :toDate";
        TypedQuery<Employee> query = entityManager.createQuery(jpql, Employee.class);

        // Set parameters
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);

        // Return the list of results
        return query.getResultList();
    }
    public List<Employee> searchEmployeesByRequest(LocalDate fromDate, LocalDate toDate) {
        String jpql = "SELECT e FROM Employee e WHERE e.DOB BETWEEN :fromDate AND :toDate";
        TypedQuery<Employee> query = entityManager.createQuery(jpql, Employee.class);

        // Set parameters
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);

        // Return the list of results
        return query.getResultList();
    }
    public Page<Employee> findByLikeCriteria(EmployeeSearchRequest request) {
        // Initialize CriteriaBuilder and CriteriaQuery
        Pageable pageable = PageRequest.of(
                request.getPage() != null ? request.getPage() : 0,
                request.getSize() != null ? request.getSize() : 10);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);

        // Create a list to hold dynamic predicates
        List<Predicate> predicates = new ArrayList<>();

        // Check and add predicates based on request fields
        if (request != null) {
            if (request.getName() != null && !request.getName().trim().isEmpty()) {
                String name = "%" + request.getName().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), name));
            }
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                String email = "%" + request.getEmail().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("email")), email));
            }
            if (request.getGender() != null && !request.getGender().trim().isEmpty()) {
                String gender = request.getGender().trim().toLowerCase();
                predicates.add(cb.like(cb.lower(root.get("gender")), gender));
            }
            if (request.getLocation() != null && !request.getLocation().trim().isEmpty()) {
                String location = "%" + request.getLocation().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("location")), location));
            }
            if (request.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("DOB"), request.getFromDate()));
            }
            if (request.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("DOB"), request.getToDate()));
            }
        }

        // Combine all predicates into the where clause
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        // Create the query with pagination
        TypedQuery<Employee> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset()); // Set offset
        query.setMaxResults(pageable.getPageSize()); // Set limit

        // Fetch the paginated results
        List<Employee> employees = query.getResultList();

        // Create a count query to determine the total number of results
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Employee> countRoot = countQuery.from(Employee.class);
        countQuery.select(cb.count(countRoot)).where(cb.and(predicates.toArray(new Predicate[0])));
        long total = entityManager.createQuery(countQuery).getSingleResult();

        // Return paginated results as a Page object
        return new PageImpl<>(employees, pageable, total);
    }
    public List<Employee> searchEmployeesByJson(EmployeeSearchRequest request) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);


        List<Predicate> predicates = new ArrayList<>();

        // Check and add predicates based on request fields
        if (request != null) {
            if (request.getName() != null && !request.getName().trim().isEmpty()) {
                String name = "%" + request.getName().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), name));
            }
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                String email = "%" + request.getEmail().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("email")), email));
            }
            if (request.getGender() != null && !request.getGender().trim().isEmpty()) {
                String gender = request.getGender().trim().toLowerCase();
                predicates.add(cb.like(cb.lower(root.get("gender")), gender));
            }
            if (request.getLocation() != null && !request.getLocation().trim().isEmpty()) {
                String location = "%" + request.getLocation().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("location")), location));
            }
            if (request.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("DOB"), request.getFromDate()));
            }
            if (request.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("DOB"), request.getToDate()));
            }
        }

        // Combine all predicates into the where clause
        cq.where(cb.and(predicates.toArray(new Predicate[0])));


        TypedQuery<Employee> query = entityManager.createQuery(cq);


        return query.getResultList();
    }

//    public Page<Employee> searchEmployeesByJson(EmployeeSearchRequest request) {
//        // Initialize CriteriaBuilder and CriteriaQuery
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
//        Root<Employee> root = cq.from(Employee.class);
//
//        // Create a list to hold dynamic predicates
//        List<Predicate> predicates = new ArrayList<>();
//
//        // Check and add predicates based on request fields
//        if (request != null) {
//            if (request.getName() != null && !request.getName().trim().isEmpty()) {
//                String name = "%" + request.getName().trim().toLowerCase() + "%";
//                predicates.add(cb.like(cb.lower(root.get("name")), name));
//            }
//            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
//                String email = "%" + request.getEmail().trim().toLowerCase() + "%";
//                predicates.add(cb.like(cb.lower(root.get("email")), email));
//            }
//            if (request.getGender() != null && !request.getGender().trim().isEmpty()) {
//                String gender = request.getGender().trim().toLowerCase();
//                predicates.add(cb.like(cb.lower(root.get("gender")), gender));
//            }
//            if (request.getLocation() != null && !request.getLocation().trim().isEmpty()) {
//                String location = "%" + request.getLocation().trim().toLowerCase() + "%";
//                predicates.add(cb.like(cb.lower(root.get("location")), location));
//            }
//            if (request.getFromDate() != null) {
//                predicates.add(cb.greaterThanOrEqualTo(root.get("DOB"), request.getFromDate()));
//            }
//            if (request.getToDate() != null) {
//                predicates.add(cb.lessThanOrEqualTo(root.get("DOB"), request.getToDate()));
//            }
//        }
//
//        // Combine all predicates into the where clause
//        cq.where(cb.and(predicates.toArray(new Predicate[0])));
//
//        // Create a count query to determine the total number of results
//        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
//        Root<Employee> countRoot = countQuery.from(Employee.class);
//        countQuery.select(cb.count(countRoot)).where(cb.and(predicates.toArray(new Predicate[0])));
//        long total = entityManager.createQuery(countQuery).getSingleResult();
//
//        // Handle pagination
//        Pageable pageable = PageRequest.of(
//                request.getPage() != null ? request.getPage() : 0,
//                request.getSize() != null ? request.getSize() : 10
//        );
//
//        // Create the query with pagination
//        TypedQuery<Employee> query = entityManager.createQuery(cq);
//        query.setFirstResult((int) pageable.getOffset()); // Set offset
//        query.setMaxResults(pageable.getPageSize()); // Set limit
//
//        // Fetch the paginated results
//        List<Employee> employees = query.getResultList();
//
//        // Return paginated results as a Page object
//        return new PageImpl<>(employees, pageable, total);
//    }


}
