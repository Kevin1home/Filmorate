package com.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.filmorate.service.util.Validator;
import com.filmorate.excepsion.IncorrectParameterException;
import com.filmorate.excepsion.NotFoundException;
import com.filmorate.model.Film;
import com.filmorate.model.FilmLike;
import com.filmorate.model.GenreType;
import com.filmorate.model.RatingType;
import com.filmorate.storage.film.FilmStorage;

import java.util.List;

/**
 * Service class for managing films, likes, genres, and ratings.<br>
 * Handles validation and delegates storage operations to {@link FilmStorage}.
 */
@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer filmId) {
        Validator.validateId(filmId, "filmId");
        return filmStorage.getFilmById(filmId);
    }

    public Film addFilm(Film film) {
        Validator.validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        Validator.validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    public List<FilmLike> getLikesByFilmId(Integer filmId) {
        Validator.validateId(filmId, "filmId");
        return filmStorage.getLikesByFilmId(filmId);
    }


    public FilmLike addLike(Integer filmId, Integer userId) {
        Validator.validateId(filmId, "filmId");
        Validator.validateId(userId, "userId");

        return filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        Validator.validateId(filmId, "filmId");
        Validator.validateId(userId, "userId");

        filmStorage.deleteLike(filmId, userId);
        log.info("User with ID {} removed like from film with ID {}", userId, filmId);
    }

    public List<Film> findMostPopularFilms(int count) {
        if (count <= 0) {
            log.warn("IncorrectParameterException! Count is negative: {}", count);
            throw new IncorrectParameterException("count");
        }
        return filmStorage.findMostPopularFilms(count);
    }

    public List<GenreType> findAllGenres() {
        return filmStorage.findAllGenres();
    }

    public GenreType findGenreById(Integer genreId) {
        Validator.validateId(genreId, "genreId");
        return filmStorage.findGenreById(genreId);
    }

    public List<GenreType> findGenreByFilmId(Integer filmId) {
        Validator.validateId(filmId, "filmId");
        return filmStorage.findGenreByFilmId(filmId);
    }

    public List<GenreType> addGenreToFilm(Integer filmId, List<String> genreList) {
        Validator.validateId(filmId, "filmId");
        if (genreList == null || genreList.isEmpty()) {
            log.warn("NotFoundException! GenreList is empty or null");
            throw new NotFoundException("GenreList is empty or null");
        }
        return filmStorage.addGenreToFilm(filmId, genreList);
    }

    public List<RatingType> findAllRatings() {
        return filmStorage.findAllRatings();
    }

    public RatingType findRatingById(Integer ratingId) {
        Validator.validateId(ratingId, "ratingId");
        return filmStorage.findRatingById(ratingId);
    }

    public RatingType findRatingByFilmId(Integer filmId) {
        Validator.validateId(filmId, "filmId");
        return filmStorage.findRatingById(filmId);
    }
}
