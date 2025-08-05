package com.example.Enterprise_Resource_Planning.hr.exception;

/**
 * Exception thrown when a department is not found.
 */
public class DepartmentNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new DepartmentNotFoundException with the specified detail message.
     * 
     * @param message the detail message
     */
    public DepartmentNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new DepartmentNotFoundException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public DepartmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
