package com.hirable.exception;

/**
 * Exception thrown when an invalid file format is uploaded
 */
public class InvalidFileFormatException extends HirableException {
    public InvalidFileFormatException(String message) {
        super(message);
    }
}
