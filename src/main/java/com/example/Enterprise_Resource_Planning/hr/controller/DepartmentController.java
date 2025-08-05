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

import com.example.Enterprise_Resource_Planning.hr.model.Department;
import com.example.Enterprise_Resource_Planning.hr.service.DepartmentService;

/**
 * REST Controller for Department management.
 * Handles HTTP requests for CRUD operations on departments.
 * 
 * All exceptions are handled by the GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/departments")
public class DepartmentController {
    
    private final DepartmentService departmentService;

    /**
     * Constructor for DepartmentController.
     * 
     * @param departmentService the department service
     */
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * Retrieves all departments.
     * 
     * @return ResponseEntity containing list of all departments
     */
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.findAll();
        return ResponseEntity.ok(departments);
    }

    /**
     * Creates a new department.
     * 
     * @param department the department to create
     * @return ResponseEntity containing the created department with 201 CREATED status
     */
    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        Department createdDepartment = departmentService.createDepartment(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    /**
     * Retrieves a specific department by ID.
     * 
     * @param id the department ID
     * @return ResponseEntity containing the department
     */
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Department department = departmentService.findById(id);
        return ResponseEntity.ok(department);
    }

    /**
     * Deletes a department by ID.
     * 
     * @param id the ID of the department to delete
     * @return ResponseEntity with 204 NO CONTENT status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates an existing department.
     * 
     * @param id the ID of the department to update
     * @param department the updated department data
     * @return ResponseEntity containing the updated department
     */
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        Department updatedDepartment = departmentService.updateDepartment(id, department);
        return ResponseEntity.ok(updatedDepartment);
    }
}
