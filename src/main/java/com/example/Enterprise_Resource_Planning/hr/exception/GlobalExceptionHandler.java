package com.example.Enterprise_Resource_Planning.hr.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for HR module exceptions.
 * Provides centralized exception handling across all controllers.
 */
@ControllerAdvice(basePackages = "com.example.Enterprise_Resource_Planning.hr.controller")
public class GlobalExceptionHandler {

    /**
     * Handle JobTitleNotFoundException.
     * Returns 404 NOT FOUND status.
     */
    @ExceptionHandler(JobTitleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleJobTitleNotFoundException(
            JobTitleNotFoundException ex, WebRequest request) {

        Map<String, Object> errorResponse = createErrorResponse(
                "JOB_TITLE_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                request.getDescription(false));

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle InvalidJobTitleException.
     * Returns 400 BAD REQUEST status.
     */
    @ExceptionHandler(InvalidJobTitleException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJobTitleException(
            InvalidJobTitleException ex, WebRequest request) {

        Map<String, Object> errorResponse = createErrorResponse(
                "INVALID_JOB_TITLE",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                request.getDescription(false));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle DepartmentNotFoundException.
     * Returns 404 NOT FOUND status.
     */
    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDepartmentNotFoundException(
            DepartmentNotFoundException ex, WebRequest request) {

        Map<String, Object> errorResponse = createErrorResponse(
                "DEPARTMENT_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                request.getDescription(false));

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle InvalidDepartmentException.
     * Returns 400 BAD REQUEST status.
     */
    @ExceptionHandler(InvalidDepartmentException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidDepartmentException(
            InvalidDepartmentException ex, WebRequest request) {

        Map<String, Object> errorResponse = createErrorResponse(
                "INVALID_DEPARTMENT",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                request.getDescription(false));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle EmployeeNotFoundException.
     * Returns 404 NOT FOUND status.
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeNotFoundException(
            EmployeeNotFoundException ex, WebRequest request) {

        Map<String, Object> errorResponse = createErrorResponse(
                "EMPLOYEE_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                request.getDescription(false));

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle InvalidEmployeeException.
     * Returns 400 BAD REQUEST status.
     */
    @ExceptionHandler(InvalidEmployeeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidEmployeeException(
            InvalidEmployeeException ex, WebRequest request) {

        Map<String, Object> errorResponse = createErrorResponse(
                "INVALID_EMPLOYEE",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                request.getDescription(false));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle all other runtime exceptions.
     * Returns 500 INTERNAL SERVER ERROR status.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {

        Map<String, Object> errorResponse = createErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getDescription(false));

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle all other general exceptions.
     * Returns 500 INTERNAL SERVER ERROR status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex, WebRequest request) {

        Map<String, Object> errorResponse = createErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getDescription(false));

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates a standardized error response.
     */
    private Map<String, Object> createErrorResponse(String errorCode, String message,
            HttpStatus status, String path) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("errorCode", errorCode);
        errorResponse.put("message", message);
        errorResponse.put("path", path);
        return errorResponse;
    }
}
