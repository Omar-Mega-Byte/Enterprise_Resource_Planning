package com.example.Enterprise_Resource_Planning.hr.validation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.example.Enterprise_Resource_Planning.hr.exception.InvalidEmployeeException;
import com.example.Enterprise_Resource_Planning.hr.model.Employee;

/**
 * Validator class for Employee entities.
 * Provides comprehensive validation logic for employee data.
 */
@Component
public class EmployeeValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9]{10,15}$"
    );
    
    private static final Pattern NATIONAL_ID_PATTERN = Pattern.compile(
        "^[A-Za-z0-9]{5,20}$"
    );

    /**
     * Validates an employee entity for creation or update.
     * 
     * @param employee the employee to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validateEmployee(Employee employee) {
        if (employee == null) {
            throw new InvalidEmployeeException("Employee cannot be null");
        }
        
        validateFullName(employee.getFullName());
        validateNationalId(employee.getNationalId());
        validateEmail(employee.getEmail());
        validatePhone(employee.getPhone());
        validateGender(employee.getGender());
        validateDateOfBirth(employee.getDateOfBirth());
        validateHireDate(employee.getHireDate());
        validateDepartment(employee.getDepartment());
        validateJobTitle(employee.getJobTitle());
        validateSalary(employee.getSalary());
        validateStatus(employee.getStatus());
        validateAddress(employee.getAddress());
        validateNotes(employee.getNotes());
        
        // Additional business rules
        validateAgeRequirement(employee.getDateOfBirth());
        validateHireDateNotInFuture(employee.getHireDate());
        validateBirthDateBeforeHireDate(employee.getDateOfBirth(), employee.getHireDate());
    }

    /**
     * Validates employee full name.
     * 
     * @param fullName the full name to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validateFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new InvalidEmployeeException("Employee full name is required");
        }
        
        if (fullName.trim().length() < 2) {
            throw new InvalidEmployeeException("Employee full name must be at least 2 characters long");
        }
        
        if (fullName.length() > 100) {
            throw new InvalidEmployeeException("Employee full name cannot exceed 100 characters");
        }
        
        // Check for valid characters (letters, spaces, hyphens, apostrophes)
        if (!fullName.matches("^[a-zA-Z\\s'-]+$")) {
            throw new InvalidEmployeeException("Employee full name contains invalid characters");
        }
    }

    /**
     * Validates employee national ID.
     * 
     * @param nationalId the national ID to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validateNationalId(String nationalId) {
        if (nationalId == null || nationalId.trim().isEmpty()) {
            throw new InvalidEmployeeException("Employee national ID is required");
        }
        
        if (!NATIONAL_ID_PATTERN.matcher(nationalId.trim()).matches()) {
            throw new InvalidEmployeeException("Employee national ID must be 5-20 alphanumeric characters");
        }
    }

    /**
     * Validates employee email address.
     * 
     * @param email the email to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidEmployeeException("Employee email is required");
        }
        
        if (email.length() > 100) {
            throw new InvalidEmployeeException("Employee email cannot exceed 100 characters");
        }
        
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new InvalidEmployeeException("Employee email format is invalid");
        }
    }

    /**
     * Validates employee phone number.
     * 
     * @param phone the phone number to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validatePhone(String phone) {
        if (phone != null && !phone.trim().isEmpty()) {
            if (phone.length() > 15) {
                throw new InvalidEmployeeException("Employee phone number cannot exceed 15 characters");
            }
            
            if (!PHONE_PATTERN.matcher(phone.trim().replaceAll("[\\s-()]", "")).matches()) {
                throw new InvalidEmployeeException("Employee phone number format is invalid");
            }
        }
    }

    /**
     * Validates employee gender.
     * 
     * @param gender the gender to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validateGender(Object gender) {
        if (gender == null) {
            throw new InvalidEmployeeException("Employee gender is required");
        }
    }

    /**
     * Validates employee date of birth.
     * 
     * @param dateOfBirth the date of birth to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new InvalidEmployeeException("Employee date of birth is required");
        }
        
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new InvalidEmployeeException("Employee date of birth cannot be in the future");
        }
    }

    /**
     * Validates employee hire date.
     * 
     * @param hireDate the hire date to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validateHireDate(LocalDate hireDate) {
        if (hireDate == null) {
            throw new InvalidEmployeeException("Employee hire date is required");
        }
    }

    /**
     * Validates employee department.
     * 
     * @param department the department to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validateDepartment(Object department) {
        if (department == null) {
            throw new InvalidEmployeeException("Employee department is required");
        }
    }

    /**
     * Validates employee job title.
     * 
     * @param jobTitle the job title to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validateJobTitle(Object jobTitle) {
        if (jobTitle == null) {
            throw new InvalidEmployeeException("Employee job title is required");
        }
    }

    /**
     * Validates employee salary.
     * 
     * @param salary the salary to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validateSalary(BigDecimal salary) {
        if (salary != null) {
            if (salary.compareTo(BigDecimal.ZERO) < 0) {
                throw new InvalidEmployeeException("Employee salary cannot be negative");
            }
            
            if (salary.scale() > 2) {
                throw new InvalidEmployeeException("Employee salary cannot have more than 2 decimal places");
            }
            
            // Check for reasonable maximum salary (adjust as needed)
            BigDecimal maxSalary = new BigDecimal("99999999.99");
            if (salary.compareTo(maxSalary) > 0) {
                throw new InvalidEmployeeException("Employee salary exceeds maximum allowed value");
            }
        }
    }

    /**
     * Validates employee status.
     * 
     * @param status the status to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validateStatus(Object status) {
        // Status can be null (default will be applied)
        // Enum validation is handled by JPA
    }

    /**
     * Validates employee address.
     * 
     * @param address the address to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validateAddress(String address) {
        if (address != null && address.length() > 255) {
            throw new InvalidEmployeeException("Employee address cannot exceed 255 characters");
        }
    }

    /**
     * Validates employee notes.
     * 
     * @param notes the notes to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validateNotes(String notes) {
        if (notes != null && notes.length() > 1000) {
            throw new InvalidEmployeeException("Employee notes cannot exceed 1000 characters");
        }
    }

    /**
     * Validates that employee meets minimum age requirement.
     * 
     * @param dateOfBirth the date of birth to validate
     * @throws InvalidEmployeeException if validation fails
     */
    private void validateAgeRequirement(LocalDate dateOfBirth) {
        if (dateOfBirth != null) {
            LocalDate minimumAge = LocalDate.now().minusYears(16);
            if (dateOfBirth.isAfter(minimumAge)) {
                throw new InvalidEmployeeException("Employee must be at least 16 years old");
            }
            
            LocalDate maximumAge = LocalDate.now().minusYears(100);
            if (dateOfBirth.isBefore(maximumAge)) {
                throw new InvalidEmployeeException("Employee age cannot exceed 100 years");
            }
        }
    }

    /**
     * Validates that hire date is not in the future.
     * 
     * @param hireDate the hire date to validate
     * @throws InvalidEmployeeException if validation fails
     */
    private void validateHireDateNotInFuture(LocalDate hireDate) {
        if (hireDate != null && hireDate.isAfter(LocalDate.now().plusDays(30))) {
            throw new InvalidEmployeeException("Employee hire date cannot be more than 30 days in the future");
        }
    }

    /**
     * Validates that birth date is before hire date.
     * 
     * @param dateOfBirth the date of birth
     * @param hireDate the hire date
     * @throws InvalidEmployeeException if validation fails
     */
    private void validateBirthDateBeforeHireDate(LocalDate dateOfBirth, LocalDate hireDate) {
        if (dateOfBirth != null && hireDate != null) {
            if (!dateOfBirth.isBefore(hireDate)) {
                throw new InvalidEmployeeException("Employee date of birth must be before hire date");
            }
        }
    }

    /**
     * Validates employee ID for update operations.
     * 
     * @param id the employee ID to validate
     * @throws InvalidEmployeeException if validation fails
     */
    public void validateEmployeeId(Long id) {
        if (id == null) {
            throw new InvalidEmployeeException("Employee ID cannot be null");
        }
        
        if (id <= 0) {
            throw new InvalidEmployeeException("Employee ID must be a positive number");
        }
    }
}
