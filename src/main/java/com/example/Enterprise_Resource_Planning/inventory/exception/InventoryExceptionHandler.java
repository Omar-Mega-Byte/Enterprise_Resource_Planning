package com.example.Enterprise_Resource_Planning.inventory.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for inventory module.
 * Handles all inventory-related exceptions and provides proper HTTP responses.
 */
@RestControllerAdvice(basePackages = "com.example.Enterprise_Resource_Planning.inventory")
@Slf4j
public class InventoryExceptionHandler {

    /**
     * Error response structure.
     */
    public static class ErrorResponse {
        private String message;
        private String error;
        private int status;
        private LocalDateTime timestamp;
        private Map<String, String> details;

        public ErrorResponse(String message, String error, int status) {
            this.message = message;
            this.error = error;
            this.status = status;
            this.timestamp = LocalDateTime.now();
            this.details = new HashMap<>();
        }

        // Getters and setters
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
        
        public Map<String, String> getDetails() { return details; }
        public void setDetails(Map<String, String> details) { this.details = details; }
    }

    /**
     * Handle product not found exceptions.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        log.warn("Product not found: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            "PRODUCT_NOT_FOUND",
            HttpStatus.NOT_FOUND.value()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle duplicate SKU exceptions.
     */
    @ExceptionHandler(DuplicateSkuException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateSkuException(DuplicateSkuException ex) {
        log.warn("Duplicate SKU: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            "DUPLICATE_SKU",
            HttpStatus.CONFLICT.value()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handle invalid product data exceptions.
     */
    @ExceptionHandler(InvalidProductDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidProductDataException(InvalidProductDataException ex) {
        log.warn("Invalid product data: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            "INVALID_PRODUCT_DATA",
            HttpStatus.BAD_REQUEST.value()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle invalid SKU exceptions.
     */
    @ExceptionHandler(InvalidSkuException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSkuException(InvalidSkuException ex) {
        log.warn("Invalid SKU: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            "INVALID_SKU",
            HttpStatus.BAD_REQUEST.value()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Bean Validation errors (from @Valid annotations).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Validation failed",
            "VALIDATION_ERROR",
            HttpStatus.BAD_REQUEST.value()
        );
        
        // Extract field validation errors
        Map<String, String> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getDefaultMessage,
                (existing, replacement) -> existing
            ));
        
        errorResponse.setDetails(fieldErrors);
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle constraint validation errors (from @Min, @NotBlank, etc. on parameters).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("Constraint validation failed: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Constraint validation failed",
            "CONSTRAINT_VIOLATION",
            HttpStatus.BAD_REQUEST.value()
        );
        
        // Extract constraint violation details
        Map<String, String> violations = ex.getConstraintViolations()
            .stream()
            .collect(Collectors.toMap(
                violation -> violation.getPropertyPath().toString(),
                ConstraintViolation::getMessage,
                (existing, replacement) -> existing
            ));
        
        errorResponse.setDetails(violations);
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle illegal argument exceptions.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            "ILLEGAL_ARGUMENT",
            HttpStatus.BAD_REQUEST.value()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle generic exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            "An unexpected error occurred",
            "INTERNAL_SERVER_ERROR",
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
