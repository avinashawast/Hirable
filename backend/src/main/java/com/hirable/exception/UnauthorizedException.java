package com.hirable.exception;

/**
 * Exception thrown when a user is not authorized to perform an action
 */
public class UnauthorizedException extends HirableException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
