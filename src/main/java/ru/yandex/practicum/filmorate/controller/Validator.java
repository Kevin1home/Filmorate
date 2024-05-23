package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.excepsions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.excepsions.NotFoundException;
import ru.yandex.practicum.filmorate.excepsions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class Validator {

    public static void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("ValidationException! Release date should not be earlier as 28 December 1895");
            throw new ValidationException("Release date is earlier as 28 December 1895.",
                    String.valueOf(film));
        }
    }

    public static void validateId(Integer id, String belongTo) {
        if (id == null) {
            log.warn("ValidationException! {} is empty", belongTo);
            throw new NotFoundException("No " + belongTo + " found in request");
        }
        if (id <= 0) {
            log.warn("ValidationException! {} is negative", belongTo);
            throw new IncorrectParameterException(belongTo);
        }
    }

    public static void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("ValidationException! Login should not contain whitespaces");
            throw new ValidationException("Login contains whitespaces",
                    String.valueOf(user));
        }
    }

}
