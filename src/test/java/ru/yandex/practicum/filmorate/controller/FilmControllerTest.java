package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.excepsions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;


class FilmControllerTest {

    private FilmController filmController;
    private Film film1;
    private Film film2;
    private Film film3;
    private Film film4;
    private Film film5;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
        initModel();
    }

    public void initModel() {
        film1 = new Film(" ", "Descr", LocalDate.of(1896, 12, 28), Duration.ofMinutes(100));
        String repeatedDescription = "Description".repeat(20);
        film2 = new Film("Name", repeatedDescription, LocalDate.of(1896, 12, 28), Duration.ofMinutes(100));
        film3 = new Film("Name", "Descr", LocalDate.of(1890, 12, 28), Duration.ofMinutes(100));
        film4 = new Film("Name", "Descr", LocalDate.of(1896, 12, 28), Duration.ofMinutes(-100));
        film5 = new Film("Name", "Descr", LocalDate.of(1896, 12, 28), Duration.ofMinutes(100));
    }

    // testing method addFilm(Film film)
    @Test
    void shouldThrowValidationExceptionByEmptyNameByAddingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film1)
        );

        Assertions.assertEquals("Название фильма не может быть пустым.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionByDescriptionWithMoreAs200CharsByAddingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film2)
        );

        Assertions.assertEquals("Максимальная длина описания — 200 символов.",
                exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionByReleaseDateAfter28121895ByAddingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film3)
        );

        Assertions.assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года.",
                exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionByNegativeDurationByAddingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film4)
        );

        Assertions.assertEquals("Продолжительность фильма должна быть положительной.",
                exception.getMessage());
    }

    // testing method addOrUpdateFilm(Film film)
    @Test
    void shouldThrowValidationExceptionByEmptyNameByAddingOrUpdatingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addOrUpdateFilm(film1)
        );

        Assertions.assertEquals("Название фильма не может быть пустым.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionByDescriptionWithMoreAs200CharsByAddingOrUpdatingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addOrUpdateFilm(film2)
        );

        Assertions.assertEquals("Максимальная длина описания — 200 символов.",
                exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionByReleaseDateAfter28121895ByAddingOrUpdatingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film3)
        );

        Assertions.assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года.",
                exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionByNegativeDurationByAddingOrUpdatingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film4)
        );

        Assertions.assertEquals("Продолжительность фильма должна быть положительной.",
                exception.getMessage());
    }

    // testing method getAllFilms()
    @Test
    void shouldAddNewFilmByAddingMethod() throws ValidationException {

        filmController.addFilm(film5);

        int expectedSize = 1;
        int actualSize = filmController.getAllFilms().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    void shouldAddNewFilmByAddingOrUpdatingMethod() throws ValidationException {

        filmController.addOrUpdateFilm(film5);

        int expectedSize = 1;
        int actualSize = filmController.getAllFilms().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    void shouldNotAddExistingFilmByAddingOrUpdatingMethod() throws ValidationException {

        filmController.addOrUpdateFilm(film5);
        filmController.addOrUpdateFilm(film5);

        int expectedSize = 1;
        int actualSize = filmController.getAllFilms().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

}