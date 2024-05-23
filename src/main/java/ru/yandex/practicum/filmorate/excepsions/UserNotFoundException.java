package ru.yandex.practicum.filmorate.excepsions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}