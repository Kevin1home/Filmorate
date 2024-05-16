package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepsions.NotFoundException;
import ru.yandex.practicum.filmorate.excepsions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getFilmStorage().getAllFilms().values().stream().toList();
    }

    @PostMapping
    public Film addFilm(@RequestBody @NotNull @Valid Film film) throws ValidationException {
        return filmService.getFilmStorage().addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @NotNull @Valid Film film) throws ValidationException {
        return filmService.getFilmStorage().updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Integer addLike(@PathVariable(value = "id", required = false) Integer filmId,
                        @PathVariable(required = false) Integer userId) {
        if (filmId == null || filmId <= 0) {
            throw new NotFoundException("filmId");
        }
        if (userId == null || userId <= 0) {
            throw new NotFoundException("userId");
        }
        filmService.addLike(filmId, userId);
        return filmId;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Integer deleteLike(@PathVariable(value = "id", required = false) Integer filmId,
                           @PathVariable(required = false) Integer userId) {
        if (filmId == null || filmId <= 0) {
            throw new NotFoundException("filmId");
        }
        if (userId == null || userId <= 0) {
            throw new NotFoundException("userId");
        }
        filmService.deleteLike(filmId, userId);
        return filmId;
    }

    @GetMapping("/popular")
    public List<Film> findMostPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count == null || count <= 0) {
            throw new NotFoundException("count");
        }
        return filmService.findMostPopularFilms(count);
    }
}
