package com.filmorate.excepsion.friendship;

/**
 * Exception thrown when a friend cannot be found by user.
 */
public class FriendshipNotFoundException extends RuntimeException {
    public FriendshipNotFoundException(String message) {
        super(message);
    }
}