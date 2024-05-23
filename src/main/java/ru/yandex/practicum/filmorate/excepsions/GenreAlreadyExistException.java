package ru.yandex.practicum.filmorate.excepsions;

public class GenreAlreadyExistException extends RuntimeException {
    public GenreAlreadyExistException(String message) {
        super(message);
    }

}