package com.example.Enterprise_Resource_Planning.inventory.exception;

/**
 * Exception thrown when invalid product data is provided.
 * This includes validation errors and business rule violations.
 */
public class InvalidProductDataException extends RuntimeException {
    
    /**
     * Constructs a new InvalidProductDataException with the specified detail message.
     * 
     * @param message the detail message
     */
    public InvalidProductDataException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new InvalidProductDataException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public InvalidProductDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
