package com.example.Enterprise_Resource_Planning.hr.validation;

import org.springframework.stereotype.Component;

import com.example.Enterprise_Resource_Planning.hr.exception.InvalidDepartmentException;
import com.example.Enterprise_Resource_Planning.hr.model.Department;

/**
 * Validator class for Department entities.
 * Provides comprehensive validation logic for department data.
 */
@Component
public class DepartmentValidator {

    /**
     * Validates a department entity for creation or update.
     * 
     * @param department the department to validate
     * @throws InvalidDepartmentException if validation fails
     */
    public void validateDepartment(Department department) {
        if (department == null) {
            throw new InvalidDepartmentException("Department cannot be null");
        }
        
        validateName(department.getName());
        validateLocation(department.getLocation());
    }

    /**
     * Validates department name.
     * 
     * @param name the department name to validate
     * @throws InvalidDepartmentException if validation fails
     */
    public void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDepartmentException("Department name is required");
        }
        
        if (name.trim().length() < 2) {
            throw new InvalidDepartmentException("Department name must be at least 2 characters long");
        }
        
        if (name.length() > 100) {
            throw new InvalidDepartmentException("Department name cannot exceed 100 characters");
        }
        
        // Check for valid characters (letters, numbers, spaces, hyphens, and common punctuation)
        if (!name.matches("^[a-zA-Z0-9\\s&'.-]+$")) {
            throw new InvalidDepartmentException("Department name contains invalid characters");
        }
        
        // Check for proper format (not just spaces or special characters)
        if (!name.trim().matches(".*[a-zA-Z0-9].*")) {
            throw new InvalidDepartmentException("Department name must contain at least one alphanumeric character");
        }
    }

    /**
     * Validates department location.
     * 
     * @param location the department location to validate
     * @throws InvalidDepartmentException if validation fails
     */
    public void validateLocation(String location) {
        if (location != null && !location.trim().isEmpty()) {
            if (location.length() > 100) {
                throw new InvalidDepartmentException("Department location cannot exceed 100 characters");
            }
            
            // Check for valid characters (letters, numbers, spaces, common punctuation for addresses)
            if (!location.matches("^[a-zA-Z0-9\\s,.'#-]+$")) {
                throw new InvalidDepartmentException("Department location contains invalid characters");
            }
            
            if (location.trim().length() < 2) {
                throw new InvalidDepartmentException("Department location must be at least 2 characters long if provided");
            }
        }
    }

    /**
     * Validates department ID for update and delete operations.
     * 
     * @param id the department ID to validate
     * @throws InvalidDepartmentException if validation fails
     */
    public void validateDepartmentId(Long id) {
        if (id == null) {
            throw new InvalidDepartmentException("Department ID cannot be null");
        }
        
        if (id <= 0) {
            throw new InvalidDepartmentException("Department ID must be a positive number");
        }
    }

    /**
     * Validates department for creation (no ID should be present).
     * 
     * @param department the department to validate for creation
     * @throws InvalidDepartmentException if validation fails
     */
    public void validateDepartmentForCreation(Department department) {
        validateDepartment(department);
        
        if (department.getId() != null) {
            throw new InvalidDepartmentException("Department ID should not be provided for creation");
        }
    }

    /**
     * Validates department for update (ID must be present).
     * 
     * @param department the department to validate for update
     * @throws InvalidDepartmentException if validation fails
     */
    public void validateDepartmentForUpdate(Department department) {
        validateDepartment(department);
        validateDepartmentId(department.getId());
    }

    /**
     * Validates that department name is unique (business rule validation).
     * Note: This method provides the validation logic but should be used in conjunction
     * with repository checks in the service layer.
     * 
     * @param name the department name to validate for uniqueness
     * @throws InvalidDepartmentException if validation fails
     */
    public void validateDepartmentNameForUniqueness(String name) {
        validateName(name);
        // Additional uniqueness validation would be performed in the service layer
        // with repository checks
    }

    /**
     * Validates department name for search operations.
     * 
     * @param name the department name to validate for search
     * @throws InvalidDepartmentException if validation fails
     */
    public void validateDepartmentNameForSearch(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDepartmentException("Department name for search cannot be null or empty");
        }
        
        if (name.trim().length() < 1) {
            throw new InvalidDepartmentException("Department name for search must be at least 1 character long");
        }
        
        if (name.length() > 100) {
            throw new InvalidDepartmentException("Department name for search cannot exceed 100 characters");
        }
    }
}
