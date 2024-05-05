package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepsions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Текущее количество фильмов: {}", films.size());
        return films.values().stream().toList();
    }

    @PostMapping
    public Film addFilm(@RequestBody @NonNull Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.warn("ValidationException! Указано пустое название фильма!");
            throw new ValidationException("Название фильма не может быть пустым.", film.getName());
        } else if (film.getDescription().length() > 200) {
            log.warn("ValidationException! Длина описания больше 200 символов!");
            throw new ValidationException("Максимальная длина описания — 200 символов.",
                    String.valueOf(film.getDescription().length()));
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("ValidationException! Дата релиза раньше 28 декабря 1895 года!");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.",
                    String.valueOf(film.getReleaseDate()));
        }else if (film.getDuration().isNegative()) {
            log.warn("ValidationException! Продолжительность фильма отрицательная!");
            throw new ValidationException("Продолжительность фильма должна быть положительной.",
                String.valueOf(film.getDuration()));
        } else {
            films.put(film.getId(), film);
        }
        log.info("Сохранённый фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film addOrUpdateFilm(@RequestBody @NonNull Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.warn("ValidationException! Указано пустое название фильма!");
            throw new ValidationException("Название фильма не может быть пустым.", film.getName());
        } else if (film.getDescription().length() > 200) {
            log.warn("ValidationException! Длина описания больше 200 символов!");
            throw new ValidationException("Максимальная длина описания — 200 символов.",
                    String.valueOf(film.getDescription().length()));
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("ValidationException! Дата релиза раньше 28 декабря 1895 года!");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.",
                    String.valueOf(film.getReleaseDate()));
        }else if (film.getDuration().isNegative()) {
            log.warn("ValidationException! Продолжительность фильма отрицательная!");
            throw new ValidationException("Продолжительность фильма должна быть положительной.",
                    String.valueOf(film.getDuration()));
        } else {
            films.put(film.getId(), film);
        }
        log.info("Сохранённый фильм: {}", film);
        return film;
    }
}
