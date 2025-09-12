package com.filmorate.excepsion.rating;

/**
 * Exception thrown when rating cannot be found.
 */
public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException(String message) {
        super(message);
    }
}