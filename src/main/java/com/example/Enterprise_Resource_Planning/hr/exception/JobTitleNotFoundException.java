package com.example.Enterprise_Resource_Planning.hr.exception;

/**
 * Exception thrown when a job title is not found.
 */
public class JobTitleNotFoundException extends RuntimeException {

    public JobTitleNotFoundException(String message) {
        super(message);
    }

    public JobTitleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
