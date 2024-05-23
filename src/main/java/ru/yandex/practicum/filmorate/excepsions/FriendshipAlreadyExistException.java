package ru.yandex.practicum.filmorate.excepsions;

public class FriendshipAlreadyExistException extends RuntimeException {
    public FriendshipAlreadyExistException(String message) {
        super(message);
    }

}