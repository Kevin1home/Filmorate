package ru.yandex.practicum.filmorate.excepsions;

public class FilmLikeNotFoundException extends RuntimeException {
    public FilmLikeNotFoundException(String message) {
        super(message);
    }
}