package com.example.Enterprise_Resource_Planning.hr.exception;

/**
 * Exception thrown when invalid employee data is provided.
 */
public class InvalidEmployeeException extends RuntimeException {
    
    /**
     * Constructs a new InvalidEmployeeException with the specified detail message.
     * 
     * @param message the detail message
     */
    public InvalidEmployeeException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new InvalidEmployeeException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public InvalidEmployeeException(String message, Throwable cause) {
        super(message, cause);
    }
}
