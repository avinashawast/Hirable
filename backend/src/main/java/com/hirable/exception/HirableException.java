package com.hirable.exception;

/**
 * Base exception class for all Hirable system exceptions
 */
public class HirableException extends RuntimeException {
    public HirableException(String message) {
        super(message);
    }

    public HirableException(String message, Throwable cause) {
        super(message, cause);
    }
}
