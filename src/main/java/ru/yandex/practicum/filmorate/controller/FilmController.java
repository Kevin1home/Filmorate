package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.GenreType;
import ru.yandex.practicum.filmorate.model.RatingType;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(required = false, value = "id") Integer filmId) {
        log.info("Received GET request: getFilmById");
        return filmService.getFilmById(filmId);
    }

    @PostMapping
    public Film addFilm(@RequestBody @NotNull @Valid Film film) {
        log.info("Received POST request: addFilm");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @NotNull @Valid Film film) {
        log.info("Received PUT request: updateFilm");
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}/like")
    public List<FilmLike> getLikesByFilmId(@PathVariable(required = false, value = "id") Integer filmId) {
        log.info("Received GET request: getLikesByFilmId");
        return filmService.getLikesByFilmId(filmId);
    }

    @PostMapping("/{id}/like/{userId}")
    public FilmLike addLike(@PathVariable(value = "id", required = false) Integer filmId,
                            @PathVariable(required = false) Integer userId) {
        log.info("Received POST request: addLike");
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseTransfer deleteLike(@PathVariable(value = "id", required = false) Integer filmId,
                                       @PathVariable(required = false) Integer userId) {
        log.info("Received DELETE request: deleteLike");
        filmService.deleteLike(filmId, userId);
        return new ResponseTransfer("Like deleted");
    }

    @GetMapping("/popular")
    public List<Film> findMostPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Received GET request: findMostPopularFilms");
        return filmService.findMostPopularFilms(count);
    }

    @GetMapping("/genres")
    public List<GenreType> findAllGenres() {
        log.info("Received GET request: findAllGenres");
        return filmService.findAllGenres();
    }

    @GetMapping("/genres/{id}")
    public GenreType findGenreById(@PathVariable(value = "id", required = false) Integer genreId) {
        log.info("Received GET request: findGenreById");
        return filmService.findGenreById(genreId);
    }

    @GetMapping("/{id}/genres")
    public List<GenreType> findGenreByFilmId(@PathVariable(value = "id", required = false) Integer filmId) {
        log.info("Received GET request: findGenreByFilmId");
        return filmService.findGenreByFilmId(filmId);
    }

    @PostMapping("/{id}/genres")
    public List<GenreType> addGenreToFilm(@PathVariable(value = "id", required = false) Integer filmId,
                                            @RequestBody @NotNull List<String> genreList) {
        log.info("Received POST request: addGenreByFilmId");
        return filmService.addGenreToFilm(filmId, genreList);
    }

    @GetMapping("/mpa")
    public List<RatingType> findAllRatings() {
        log.info("Received GET request: findAllRatings");
        return filmService.findAllRatings();
    }

    @GetMapping("/mpa/{id}")
    public RatingType findRatingById(@PathVariable(value = "id", required = false) Integer ratingId) {
        log.info("Received GET request: findRatingById");
        return filmService.findRatingById(ratingId);
    }

    @GetMapping("{id}/mpa")
    public RatingType findRatingByFilmId(@PathVariable(value = "id", required = false) Integer ratingId) {
        log.info("Received GET request: findRatingByFilmId");
        return filmService.findRatingByFilmId(ratingId);
    }

}
