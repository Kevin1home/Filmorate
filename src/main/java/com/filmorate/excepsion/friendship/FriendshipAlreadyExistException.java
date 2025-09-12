package com.filmorate.excepsion.friendship;

/**
 * Exception thrown when trying to add a friend that already exists by user.
 */
public class FriendshipAlreadyExistException extends RuntimeException {
    public FriendshipAlreadyExistException(String message) {
        super(message);
    }

}