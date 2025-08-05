package com.example.Enterprise_Resource_Planning.hr.validation;

import org.springframework.stereotype.Component;

import com.example.Enterprise_Resource_Planning.hr.exception.InvalidJobTitleException;
import com.example.Enterprise_Resource_Planning.hr.model.JobTitle;

/**
 * Validator class for JobTitle entities.
 * Provides comprehensive validation logic for job title data.
 */
@Component
public class JobTitleValidator {

    /**
     * Validates a job title entity for creation or update.
     * 
     * @param jobTitle the job title to validate
     * @throws InvalidJobTitleException if validation fails
     */
    public void validateJobTitle(JobTitle jobTitle) {
        if (jobTitle == null) {
            throw new InvalidJobTitleException("Job title cannot be null");
        }
        
        validateTitle(jobTitle.getTitle());
        validateDescription(jobTitle.getDescription());
    }

    /**
     * Validates job title name.
     * 
     * @param title the job title name to validate
     * @throws InvalidJobTitleException if validation fails
     */
    public void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidJobTitleException("Job title name is required");
        }
        
        if (title.trim().length() < 2) {
            throw new InvalidJobTitleException("Job title name must be at least 2 characters long");
        }
        
        if (title.length() > 100) {
            throw new InvalidJobTitleException("Job title name cannot exceed 100 characters");
        }
        
        // Check for valid characters (letters, numbers, spaces, hyphens, slashes, and common punctuation)
        if (!title.matches("^[a-zA-Z0-9\\s&'./()-]+$")) {
            throw new InvalidJobTitleException("Job title name contains invalid characters");
        }
        
        // Check for proper format (not just spaces or special characters)
        if (!title.trim().matches(".*[a-zA-Z0-9].*")) {
            throw new InvalidJobTitleException("Job title name must contain at least one alphanumeric character");
        }
        
        // Additional business rules for job titles
        validateJobTitleFormat(title.trim());
    }

    /**
     * Validates job title description.
     * 
     * @param description the job title description to validate
     * @throws InvalidJobTitleException if validation fails
     */
    public void validateDescription(String description) {
        if (description != null && !description.trim().isEmpty()) {
            if (description.length() > 255) {
                throw new InvalidJobTitleException("Job title description cannot exceed 255 characters");
            }
            
            if (description.trim().length() < 5) {
                throw new InvalidJobTitleException("Job title description must be at least 5 characters long if provided");
            }
            
            // Allow more characters in description including newlines and additional punctuation
            if (!description.matches("^[a-zA-Z0-9\\s,.!?'\"&/()\\-:;\\n\\r]+$")) {
                throw new InvalidJobTitleException("Job title description contains invalid characters");
            }
        }
    }

    /**
     * Validates job title ID for update and delete operations.
     * 
     * @param id the job title ID to validate
     * @throws InvalidJobTitleException if validation fails
     */
    public void validateJobTitleId(Long id) {
        if (id == null) {
            throw new InvalidJobTitleException("Job title ID cannot be null");
        }
        
        if (id <= 0) {
            throw new InvalidJobTitleException("Job title ID must be a positive number");
        }
    }

    /**
     * Validates job title for creation (no ID should be present).
     * 
     * @param jobTitle the job title to validate for creation
     * @throws InvalidJobTitleException if validation fails
     */
    public void validateJobTitleForCreation(JobTitle jobTitle) {
        validateJobTitle(jobTitle);
        
        if (jobTitle.getId() != null) {
            throw new InvalidJobTitleException("Job title ID should not be provided for creation");
        }
    }

    /**
     * Validates job title for update (ID must be present).
     * 
     * @param jobTitle the job title to validate for update
     * @throws InvalidJobTitleException if validation fails
     */
    public void validateJobTitleForUpdate(JobTitle jobTitle) {
        validateJobTitle(jobTitle);
        validateJobTitleId(jobTitle.getId());
    }

    /**
     * Validates that job title name is unique (business rule validation).
     * Note: This method provides the validation logic but should be used in conjunction
     * with repository checks in the service layer.
     * 
     * @param title the job title name to validate for uniqueness
     * @throws InvalidJobTitleException if validation fails
     */
    public void validateJobTitleForUniqueness(String title) {
        validateTitle(title);
        // Additional uniqueness validation would be performed in the service layer
        // with repository checks
    }

    /**
     * Validates job title for search operations.
     * 
     * @param title the job title name to validate for search
     * @throws InvalidJobTitleException if validation fails
     */
    public void validateJobTitleForSearch(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidJobTitleException("Job title for search cannot be null or empty");
        }
        
        if (title.trim().length() < 1) {
            throw new InvalidJobTitleException("Job title for search must be at least 1 character long");
        }
        
        if (title.length() > 100) {
            throw new InvalidJobTitleException("Job title for search cannot exceed 100 characters");
        }
    }

    /**
     * Validates specific job title format rules.
     * 
     * @param title the job title to validate for format
     * @throws InvalidJobTitleException if validation fails
     */
    private void validateJobTitleFormat(String title) {
        // Check for common job title patterns and restrictions
        
        // Should not start or end with special characters
        if (title.matches("^[^a-zA-Z0-9].*") || title.matches(".*[^a-zA-Z0-9]$")) {
            throw new InvalidJobTitleException("Job title cannot start or end with special characters");
        }
        
        // Should not have consecutive special characters
        if (title.matches(".*[^a-zA-Z0-9]{2,}.*")) {
            throw new InvalidJobTitleException("Job title cannot contain consecutive special characters");
        }
        
        // Check for inappropriate words or patterns (can be extended as needed)
        String lowerTitle = title.toLowerCase();
        if (lowerTitle.contains("  ")) { // Multiple consecutive spaces
            throw new InvalidJobTitleException("Job title cannot contain multiple consecutive spaces");
        }
    }

    /**
     * Validates job title hierarchy level (if applicable).
     * This can be used for roles like Senior, Junior, Lead, etc.
     * 
     * @param title the job title to validate for hierarchy
     * @throws InvalidJobTitleException if validation fails
     */
    public void validateJobTitleHierarchy(String title) {
        if (title != null) {
            // This method can be extended to validate job title hierarchies
            // For example, checking if "Senior" appears with appropriate base titles
            validateTitle(title);
            
            // Additional hierarchy validation logic can be added here
            // Example: validate that certain prefixes are used correctly
        }
    }

    /**
     * Validates job title for assignment to employees.
     * This ensures the job title is in a valid state for employee assignment.
     * 
     * @param jobTitle the job title to validate for assignment
     * @throws InvalidJobTitleException if validation fails
     */
    public void validateJobTitleForAssignment(JobTitle jobTitle) {
        validateJobTitle(jobTitle);
        
        if (jobTitle.getId() == null) {
            throw new InvalidJobTitleException("Job title must be saved before it can be assigned to employees");
        }
        
        // Additional business rules for assignment can be added here
        // For example, checking if the job title is active/enabled
    }
}
