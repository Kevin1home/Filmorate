package com.filmorate.excepsion.gender;

/**
 * Exception thrown when gender cannot be found.
 */
public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(String message) {
        super(message);
    }
}