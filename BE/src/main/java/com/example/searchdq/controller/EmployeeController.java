package com.example.searchdq.controller;

import com.example.searchdq.entity.Employee;
import com.example.searchdq.entity.EmployeeSearchRequest;
import com.example.searchdq.repository.EmployeeRepository;
import com.example.searchdq.service.EmployeeService;
import com.example.searchdq.util.IdInvalidException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

class DateRangeRequest {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate toDate;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String gender;
    private String email;
    private String location;


    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/search")
    public ResponseEntity<Page<Employee>> seachEmployee(
            @RequestBody DateRangeRequest dateRange,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size){
        Page<Employee> employees = employeeService.searchEmployees(dateRange.getName(), page, size);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/searchByDateRange")
    public ResponseEntity<Page<Employee>> searchEmployeeByDateRange(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size){
        Page<Employee> employees = employeeService.getEmployeesByDateRange(fromDate,toDate, page, size);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/searchByJson")
    public ResponseEntity<Page<Employee>> searchByDateRange(
            @RequestBody DateRangeRequest dateRange,
            @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "4") int size){
        Page<Employee> employees = employeeService.getEmployeesByDateRange(dateRange.getFromDate(), dateRange.getToDate(), page,size);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/searchJson")
    public ResponseEntity<Page<Employee>> searchEmployees(
            @RequestBody DateRangeRequest dateRange,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size){
        Page<Employee> employees = employeeService.searchEmployeeJson(dateRange.getName(),dateRange.getFromDate(),
                dateRange.getToDate(),dateRange.getLocation(),dateRange.getGender(), dateRange.getEmail(), page,size);
    return new ResponseEntity<>(employees, HttpStatus.OK);
    }
    //-----------------
    @GetMapping("/searchJsonFull")
    public Page<Employee> searchEmployees(@RequestBody EmployeeSearchRequest request) {
        return employeeService.findByLikeCriteria(request);
    }

    //----------Entity Manager
    @GetMapping("/")
    public ResponseEntity<List<Employee>> getAllEmployee(){
        List<Employee> employees = employeeService.findAllEM();
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.save(employee);
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable("id") int id,
            @RequestBody Employee employee) {

        try {
            Employee updatedEmployee = employeeService.updateEmployee(id, employee);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } catch (Exception e) {
            // In lỗi ra console và trả về status 500 nếu có vấn đề
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Integer id) throws  IdInvalidException{
        Employee currentEmployee = this.employeeService.findByIdEM(id);
        if(currentEmployee == null){
            throw new IdInvalidException("Employee khong ton tai");
        }
        return ResponseEntity.ok(employeeService.findByIdEM(id));
    }

    @PostMapping("/addNewEmployee")
    public ResponseEntity<Employee> addNewEmployee(@RequestBody Employee employee){
        return ResponseEntity.ok(employeeService.addNewEmployeeEM(employee));
    }

    @PutMapping("/edit")
    public ResponseEntity<Employee> editEmployee(@RequestBody Employee employee) throws IdInvalidException {
        Employee currentEmployee = employeeService.updateEmployeeEM(employee);
        if(currentEmployee == null){
            throw new IdInvalidException("ID khong ton tai ");
        }
        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployeeEM(id);
        return ResponseEntity.ok().body(Collections.singletonMap("message", "Xóa thành công"));
    }

    @GetMapping("/searchByDateRangeEM")
    public ResponseEntity<List<Employee>> searchEmployeeByDateRange(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate){
        List<Employee> employees = employeeService.getEmployeesByDateRangeEM(fromDate,toDate);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
    @PostMapping("/searchJsonEM")
    public Page<Employee> searchEmployeesEM(@RequestBody EmployeeSearchRequest request) {
        // Call the service to search employees with the given request
        return employeeService.searchEmployeesEM(request);
    }

    @PostMapping("/search")
    public List<Employee> searchEmployeesByRequest(@RequestBody EmployeeSearchRequest request) {
        // Call the service method with the search parameters
        return employeeService.searchEmployeesByRequest(request.getFromDate(), request.getToDate());
    }
    @PostMapping("/searchJson")
    public List<Employee> searchEmployeesJson(@RequestBody EmployeeSearchRequest request) {
        return employeeService.searchEmployeesByJson(request);
    }
    @GetMapping("/searchSQL")
    public Page<Employee> searchStudents(EmployeeSearchRequest request, Pageable pageable) {
        return employeeService.search(request, pageable);
    }



    //-----------------------------------------

//    @GetMapping("/")
//    public ResponseEntity<List<Employee>> getAllEmployee(){
//        List<Employee> employees = employeeService.getAllEmployee();
//        return ResponseEntity.ok(employees);
//    }
//
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Integer id) throws  IdInvalidException{
//        Employee currentEmployee = this.employeeService.getEmployeeById(id);
//        if(currentEmployee == null){
//            throw new IdInvalidException("Employee khong ton tai");
//        }
//        return ResponseEntity.ok(employeeService.getEmployeeById(id));
//    }
//
//    @PostMapping("/addNewEmployee")
//    public ResponseEntity<Employee> addNewEmployee(@RequestBody Employee employee){
//        return ResponseEntity.ok(this.employeeService.addNewEmployee(employee));
//    }
//
//    @PutMapping("/edit")
//    public ResponseEntity<Employee> editEmployee(@RequestBody Employee employee) throws IdInvalidException {
//        Employee currentEmployee = employeeService.updateEmployee(employee);
//        if(currentEmployee == null){
//            throw new IdInvalidException("ID khong ton tai ");
//        }
//        return ResponseEntity.ok(employee);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Integer id) throws IdInvalidException{
//        Employee currentRoom = this.employeeService.getEmployeeById(id);
//        if (currentRoom == null){
//            throw new IdInvalidException("Room voi id + " + id + "khong ton tai");
//        }
//        this.employeeService.deleteEmployee(id);
//        return ResponseEntity.ok("Xoa thanh cong phong voi id = " + id);
//    }

    //---------------------- stored procedure
    @GetMapping("/employeeCount")
    public List getTotalBlogs(){
        return employeeService.getAllEmployeeBySP();
    }

    @Transactional
    @GetMapping("/searchEmployee")
    public List<Employee> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String email) {
        return employeeService.searchEmployeesBySP(name, gender, location, email);
    }

}
