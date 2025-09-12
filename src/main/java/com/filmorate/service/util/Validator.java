package com.filmorate.service.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.filmorate.excepsion.IncorrectParameterException;
import com.filmorate.excepsion.NotFoundException;
import com.filmorate.excepsion.ValidationException;
import com.filmorate.model.Film;
import com.filmorate.model.User;

import java.time.LocalDate;

/**
 * Utility class for validating Film and User objects.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Validator {

    private static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);

    /**
     * Validates a film's release date.
     *
     * @param film the film to validate
     * @throws ValidationException if release date is before 28 December 1895
     */
    public static void validateFilm(Film film) {
        if (film == null) {
            log.warn("ValidationException! Film is null");
            throw new ValidationException("Film cannot be null", "null");
        }

        if (film.getReleaseDate() == null) {
            log.warn("ValidationException! Film release date is null: {}", film);
            throw new ValidationException("Release date cannot be null", String.valueOf(film));
        }

        if (film.getReleaseDate().isBefore(FIRST_FILM_DATE)) {
            log.warn("ValidationException! Release date {} is earlier than 28 December 1895", film.getReleaseDate());
            throw new ValidationException("Release date cannot be earlier than 28 December 1895",
                    String.valueOf(film));
        }
    }

    /**
     * Validates an ID.
     *
     * @param id the ID to validate
     * @param belongTo the name of the entity (used in exception messages)
     * @throws NotFoundException if ID is null
     * @throws IncorrectParameterException if ID is zero or negative
     */
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

    /**
     * Validates a user's login.
     *
     * @param user the user to validate
     * @throws ValidationException if login contains whitespace
     */
    public static void validateUser(User user) {
        if (user == null) {
            log.warn("ValidationException! User is null");
            throw new ValidationException("User cannot be null", "null");
        }

        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            log.warn("ValidationException! Login should not contain whitespaces");
            throw new ValidationException("Login contains whitespaces",
                    String.valueOf(user));
        }
    }
}
