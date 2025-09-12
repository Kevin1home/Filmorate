package com.filmorate.excepsion.filmLike;

/**
 * Exception thrown when trying to add a film like that already exists in the system.
 */
public class FilmLikeAlreadyExistException extends RuntimeException {
    public FilmLikeAlreadyExistException(String message) {
        super(message);
    }

}