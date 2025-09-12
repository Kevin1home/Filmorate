package com.filmorate.excepsion;

/**
 * Simple exception thrown when entity cannot be found.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
