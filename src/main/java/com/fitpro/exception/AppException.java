package com.fitpro.exception;

/**
 * Single checked exception used across the application.
 * DAO and service layers wrap low-level errors (e.g. SQLException)
 * in this class so servlets can show a friendly message.
 */
public class AppException extends Exception {
    private static final long serialVersionUID = 1L;

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
