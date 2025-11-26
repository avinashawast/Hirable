package com.hirable.exception;

/**
 * Exception thrown when a job is not found
 */
public class JobNotFoundException extends HirableException {
    public JobNotFoundException(String message) {
        super(message);
    }

    public JobNotFoundException(Long jobId) {
        super("Job with id " + jobId + " not found");
    }
}
