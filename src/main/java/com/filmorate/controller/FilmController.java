package com.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.filmorate.controller.util.ResponseTransfer;
import com.filmorate.model.Film;
import com.filmorate.model.FilmLike;
import com.filmorate.model.GenreType;
import com.filmorate.model.RatingType;
import com.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    /**
     * REST controller for managing films, likes, genres, and ratings.
     */
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("GET /films — fetching all films");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(value = "id") Integer filmId) {
        log.info("GET /films/{} — fetching film by ID", filmId);
        return filmService.getFilmById(filmId);
    }

    @PostMapping
    public Film addFilm(@RequestBody @NotNull @Valid Film film) {
        log.info("POST /films — adding film: {}", film.getName());
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @NotNull @Valid Film film) {
        log.info("PUT /films — updating film: {}", film.getId());
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}/like")
    public List<FilmLike> getLikesByFilmId(@PathVariable(value = "id") Integer filmId) {
        log.info("GET /films/{}/like — fetching likes", filmId);
        return filmService.getLikesByFilmId(filmId);
    }

    @PostMapping("/{id}/like/{userId}")
    public FilmLike addLike(@PathVariable(value = "id") Integer filmId,
                            @PathVariable Integer userId) {
        log.info("POST /films/{}/like/{} — adding like", filmId, userId);
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseTransfer deleteLike(@PathVariable(value = "id") Integer filmId,
                                       @PathVariable Integer userId) {
        log.info("DELETE /films/{}/like/{} — removing like", filmId, userId);
        filmService.deleteLike(filmId, userId);
        return new ResponseTransfer("Like deleted");
    }

    @GetMapping("/popular")
    public List<Film> findMostPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("GET /films/popular?count={} — fetching most popular films", count);
        return filmService.findMostPopularFilms(count);
    }

    @GetMapping("/genres")
    public List<GenreType> findAllGenres() {
        log.info("GET /films/genres — fetching all genres");
        return filmService.findAllGenres();
    }

    @GetMapping("/genres/{id}")
    public GenreType findGenreById(@PathVariable(value = "id") Integer genreId) {
        log.info("GET /films/genres/{} — fetching genre by ID", genreId);
        return filmService.findGenreById(genreId);
    }

    @GetMapping("/{id}/genres")
    public List<GenreType> findGenreByFilmId(@PathVariable(value = "id") Integer filmId) {
        log.info("GET /films/{}/genres — fetching genres by film ID", filmId);
        return filmService.findGenreByFilmId(filmId);
    }

    @PostMapping("/{id}/genres")
    public List<GenreType> addGenreToFilm(@PathVariable(value = "id") Integer filmId,
                                            @RequestBody @NotNull List<String> genreList) {
        log.info("POST /films/{}/genres — adding genres: {}", filmId, genreList);
        return filmService.addGenreToFilm(filmId, genreList);
    }

    @GetMapping("/mpa")
    public List<RatingType> findAllRatings() {
        log.info("GET /films/mpa — fetching all ratings");
        return filmService.findAllRatings();
    }

    @GetMapping("/mpa/{id}")
    public RatingType findRatingById(@PathVariable(value = "id") Integer ratingId) {
        log.info("GET /films/mpa/{} — fetching rating by ID", ratingId);
        return filmService.findRatingById(ratingId);
    }

    @GetMapping("{id}/mpa")
    public RatingType findRatingByFilmId(@PathVariable(value = "id") Integer filmId) {
        log.info("GET /films/{}/mpa — fetching rating by film ID", filmId);
        return filmService.findRatingByFilmId(filmId);
    }
}
