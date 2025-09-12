package com.filmorate.excepsion.filmLike;

/**
 * Exception thrown when a film like cannot be found in the system by its ID or other criteria.
 */
public class FilmLikeNotFoundException extends RuntimeException {
    public FilmLikeNotFoundException(String message) {
        super(message);
    }
}