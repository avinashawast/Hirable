package com.hirable.exception;

/**
 * Exception thrown when a user is not found
 */
public class UserNotFoundException extends HirableException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long userId) {
        super("User with id " + userId + " not found");
    }
}
