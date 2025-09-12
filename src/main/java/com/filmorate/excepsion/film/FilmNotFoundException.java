package com.filmorate.excepsion.film;

/**
 * Exception thrown when a film cannot be found in the system by its ID or other criteria.
 */
public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(String message) {
        super(message);
    }
}