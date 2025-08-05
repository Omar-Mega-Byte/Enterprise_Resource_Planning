package com.example.Enterprise_Resource_Planning.hr.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Enterprise_Resource_Planning.hr.model.Employee;
import com.example.Enterprise_Resource_Planning.hr.service.EmployeeService;

/**
 * REST Controller for Employee management.
 * Handles HTTP requests for CRUD operations on employees.
 * 
 * All exceptions are handled by the GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    
    private final EmployeeService employeeService;
    
    /**
     * Constructor for EmployeeController.
     * 
     * @param employeeService the employee service
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Retrieves all employees.
     * 
     * @return ResponseEntity containing list of all employees
     */
    @GetMapping
    public ResponseEntity<List<Employee>> findAll() {
        List<Employee> employees = employeeService.findAll();
        return ResponseEntity.ok(employees);
    }

    /**
     * Creates a new employee.
     * 
     * @param employee the employee to create
     * @return ResponseEntity containing the created employee with 201 CREATED status
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    /**
     * Retrieves a specific employee by ID.
     * 
     * @param id the employee ID
     * @return ResponseEntity containing the employee
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);
        return ResponseEntity.ok(employee);
    }

    /**
     * Retrieves a specific employee by email address.
     * 
     * @param email the employee email
     * @return ResponseEntity containing the employee
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Employee> findByEmail(@PathVariable String email) {
        Employee employee = employeeService.findByEmail(email);
        return ResponseEntity.ok(employee);
    }
    /**
     * Updates an existing employee.
     * 
     * @param id the ID of the employee to update
     * @param employee the updated employee data
     * @return ResponseEntity containing the updated employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    /**
     * Deletes an employee by ID.
     * 
     * @param id the ID of the employee to delete
     * @return ResponseEntity with 204 NO CONTENT status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
