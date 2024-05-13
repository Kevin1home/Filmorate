package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class Validator {

    protected static boolean validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("ValidationException! Дата релиза не должна быть раньше 28 декабря 1895 года.");
            return false;
        }
        return true;
    }

    protected static boolean validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("ValidationException! Логин не должен содержать пробелы.");
            return false;
        }

        return true;
    }

}
