package com.example.Enterprise_Resource_Planning.hr.exception;

/**
 * Exception thrown when an employee is not found.
 */
public class EmployeeNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new EmployeeNotFoundException with the specified detail message.
     * 
     * @param message the detail message
     */
    public EmployeeNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new EmployeeNotFoundException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
