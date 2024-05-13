package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepsions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.controller.Validator.validateFilm;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private static final HashMap<Integer, Film> films = new HashMap<>();
    private static int nextId = 1;

    @GetMapping
    public static List<Film> getAllFilms() {

        log.info("Текущее количество фильмов: {}", films.size());
        return films.values().stream().toList();
    }

    @PostMapping
    public Film addFilm(@RequestBody @NonNull Film film) throws ValidationException {

        if (!validateFilm(film)) {
            throw new ValidationException("Валидация параметров фильма не пройдена", String.valueOf(film));
        }

        for (Film filmExisting : films.values()) {
            boolean isEqual = Objects.equals(filmExisting.getDescription(), film.getDescription());
            if (isEqual) {
                throw new ValidationException("Такой фильм уже есть.", String.valueOf(film));
            }
        }

        film.setId(generateId());
        films.put(film.getId(), film);

        log.info("Сохранённый фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film addOrUpdateFilm(@RequestBody @NonNull Film film) throws ValidationException {
        if (!validateFilm(film)) {
            throw new ValidationException("Валидация параметров фильма не пройдена", String.valueOf(film));
        }

        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Такого id нет.", String.valueOf(film.getId()));
        }

        films.put(film.getId(), film);

        log.info("Обновлённый фильм: {}", film);
        return film;
    }

    public int generateId() {
        return nextId++;
    }

}
