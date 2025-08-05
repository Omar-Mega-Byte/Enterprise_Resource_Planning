package com.example.Enterprise_Resource_Planning.hr.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Enterprise_Resource_Planning.hr.exception.DepartmentNotFoundException;
import com.example.Enterprise_Resource_Planning.hr.exception.InvalidDepartmentException;
import com.example.Enterprise_Resource_Planning.hr.model.Department;
import com.example.Enterprise_Resource_Planning.hr.repository.DepartmentRepository;

/**
 * Service class for managing Department entities.
 * Provides business logic for CRUD operations on departments.
 * 
 * All exceptions are designed to be handled by the GlobalExceptionHandler.
 */
@Service
@Transactional
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;

    /**
     * Constructor for DepartmentService.
     * 
     * @param departmentRepository the department repository
     */
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
    /**
     * Creates a new department.
     * 
     * @param department the department to create
     * @return the created department
     * @throws InvalidDepartmentException if department is null
     */
    public Department createDepartment(Department department) {
        if (department == null) {
            throw new InvalidDepartmentException("Department cannot be null");
        }
        return departmentRepository.save(department);
    }

    /**
     * Retrieves all departments.
     * 
     * @return List of all departments
     */
    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    /**
     * Finds a department by its ID.
     * 
     * @param id the department ID
     * @return the department if found
     * @throws InvalidDepartmentException if id is null
     * @throws DepartmentNotFoundException if department is not found
     */
    @Transactional(readOnly = true)
    public Department findById(Long id) {
        if (id == null) {
            throw new InvalidDepartmentException("Department ID cannot be null");
        }
        return departmentRepository.findById(id)
            .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
    }

    /**
     * Deletes a department by ID.
     * 
     * @param id the ID of the department to delete
     * @throws InvalidDepartmentException if id is null
     * @throws DepartmentNotFoundException if department doesn't exist
     */
    public void deleteDepartment(Long id) {
        if (id == null) {
            throw new InvalidDepartmentException("Department ID cannot be null");
        }
        
        if (!departmentRepository.existsById(id)) {
            throw new DepartmentNotFoundException("Department with id " + id + " does not exist");
        }
        
        departmentRepository.deleteById(id);
    }

    /**
     * Updates an existing department.
     * 
     * @param id the ID of the department to update
     * @param department the updated department data
     * @return the updated department
     * @throws InvalidDepartmentException if parameters are invalid
     * @throws DepartmentNotFoundException if department doesn't exist
     */
    public Department updateDepartment(Long id, Department department) {
        if (id == null) {
            throw new InvalidDepartmentException("Department ID cannot be null");
        }
        if (department == null) {
            throw new InvalidDepartmentException("Department cannot be null");
        }
        
        return departmentRepository.findById(id)
                .map(existingDepartment -> {
                    department.setId(id);
                    return departmentRepository.save(department);
                })
                .orElseThrow(() -> new DepartmentNotFoundException("Department with id " + id + " does not exist"));
    }
}
