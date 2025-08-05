package com.example.Enterprise_Resource_Planning.hr.exception;

/**
 * Exception thrown when invalid department data is provided.
 */
public class InvalidDepartmentException extends RuntimeException {
    
    /**
     * Constructs a new InvalidDepartmentException with the specified detail message.
     * 
     * @param message the detail message
     */
    public InvalidDepartmentException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new InvalidDepartmentException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public InvalidDepartmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
