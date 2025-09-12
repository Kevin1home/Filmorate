package com.filmorate.excepsion.gender;

/**
 * Exception thrown when trying to add gender that already exists.
 */
public class GenreAlreadyExistException extends RuntimeException {
    public GenreAlreadyExistException(String message) {
        super(message);
    }

}