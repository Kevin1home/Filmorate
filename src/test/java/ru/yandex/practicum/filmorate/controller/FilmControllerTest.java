package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.excepsions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

class FilmControllerTest {

    private FilmController filmController;
    private Film film1;
    private Film film2;
    private Film film3;
    private Film film4;

    @BeforeEach
    public void setUp() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);

        initModel();
    }

    public void initModel() {
        film1 = new Film("Name", "Descr", LocalDate.of(1500, 12, 28));
        film1.setDuration(100);

        film2 = new Film("Name", "Descr", LocalDate.of(2000, 1, 1));
        film2.setDuration(100);

        film3 = new Film("Name2", "Descr2", LocalDate.of(1500, 12, 28));
        film3.setId(1);
        film3.setDuration(100);

        film4 = new Film("Name2", "Descr2", LocalDate.of(2000, 1, 1));
        film4.setId(1);
        film4.setDuration(100);
    }

    // testing method addFilm(Film film)
    @Test
    void shouldThrowValidationExceptionByReleaseDateBefore28121895ByAddingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film1)
        );

        Assertions.assertEquals("Валидация параметров фильма не пройдена.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionByAddingExistingFilmByAddingMethod() throws ValidationException {

        filmController.addFilm(film2);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(film2)
        );

        Assertions.assertEquals("Такой фильм уже есть.", exception.getMessage());
    }

    @Test
    void shouldAddFilmWithRightParameters() throws ValidationException {

        filmController.addFilm(film2);

        int expectedSize = 1;
        int actualSize = filmController.getAllFilms().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
        // testing method updateFilm(Film film)
    void shouldThrowValidationExceptionByReleaseDateBefore28121895ByUpdateMethod() throws ValidationException {

        filmController.addFilm(film2);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(film3)
        );

        Assertions.assertEquals("Валидация параметров фильма не пройдена.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionByUpdatingNotExistingFilmByUpdatingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(film4)
        );

        Assertions.assertEquals("Такого id нет.", exception.getMessage());
    }

    @Test
    void shouldUpdateFilmWithRightParameters() throws ValidationException {

        filmController.addFilm(film2);
        filmController.updateFilm(film4);

        String expectedName = "Name2";
        String actualName = filmController.getAllFilms().get(0).getName();

        Assertions.assertEquals(expectedName, actualName);
    }

    // testing method getAllFilms()
    @Test
    void shouldAddNewFilmByAddingMethod() throws ValidationException {

        filmController.addFilm(film2);

        int expectedSize = 1;
        int actualSize = filmController.getAllFilms().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    void shouldNotAddNewFilmByUpdatingMethod() throws ValidationException {

        filmController.addFilm(film2);
        filmController.updateFilm(film4);

        int expectedSize = 1;
        int actualSize = filmController.getAllFilms().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

}