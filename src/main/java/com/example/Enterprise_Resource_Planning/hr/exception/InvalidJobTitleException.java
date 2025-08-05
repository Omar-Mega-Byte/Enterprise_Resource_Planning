package com.example.Enterprise_Resource_Planning.hr.exception;

/**
 * Exception thrown when invalid job title data is provided.
 */
public class InvalidJobTitleException extends RuntimeException {

    public InvalidJobTitleException(String message) {
        super(message);
    }

    public InvalidJobTitleException(String message, Throwable cause) {
        super(message, cause);
    }
}
