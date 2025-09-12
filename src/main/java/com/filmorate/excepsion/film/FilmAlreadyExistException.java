package com.filmorate.excepsion.film;

/**
 * Exception thrown when trying to add a film that already exists in the system.
 */
public class FilmAlreadyExistException extends RuntimeException {
    public FilmAlreadyExistException(String message) {
        super(message);
    }
}