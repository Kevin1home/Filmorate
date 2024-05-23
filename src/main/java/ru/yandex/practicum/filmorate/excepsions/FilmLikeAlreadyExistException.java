package ru.yandex.practicum.filmorate.excepsions;

public class FilmLikeAlreadyExistException extends RuntimeException {
    public FilmLikeAlreadyExistException(String message) {
        super(message);
    }

}