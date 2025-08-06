package com.example.Enterprise_Resource_Planning.inventory.exception;

public class InvalidSkuException extends RuntimeException {
    public InvalidSkuException(String message) {
        super(message);
    }
    
    public InvalidSkuException(String message, Throwable cause) {
        super(message, cause);
    }
}
