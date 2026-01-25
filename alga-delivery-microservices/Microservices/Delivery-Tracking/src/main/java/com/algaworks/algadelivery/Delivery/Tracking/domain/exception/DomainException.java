package com.algaworks.algadelivery.Delivery.Tracking.domain.exception;

// Custom Exception for Domain Layer
public class DomainException extends RuntimeException {

    // Default constructor
    public DomainException() {
    }

    // Constructor with message
    public DomainException(String message) {
        super(message);
    }

    // Constructor with message and cause
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
