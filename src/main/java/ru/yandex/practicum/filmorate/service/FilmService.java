package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.Validator;
import ru.yandex.practicum.filmorate.excepsions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.excepsions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.GenreType;
import ru.yandex.practicum.filmorate.model.RatingType;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@Slf4j
@Getter
@Setter
public class FilmService {

    @Autowired
    @Qualifier("filmDbStorage")
    private FilmStorage filmStorage;

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
        log.info("Пользователь с ID {} убрал лайк фильму с ID {}", userId, filmId);
    }

    public List<Film> findMostPopularFilms(int count) {
        if (count <= 0) {
            log.warn("ValidationException! Count is negative");
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
        if (genreList.isEmpty()) {
            log.warn("ValidationException! GenreList is empty");
            throw new NotFoundException("GenreList is empty");
        }
        return filmStorage.addGenreToFilm(filmId, genreList);
    }

    public List<RatingType> findAllRatings() {
        return filmStorage.findAllRatings();
    }

    public RatingType findRatingById(Integer ratingId) {
        Validator.validateId(ratingId, "ratingId");
        return findRatingById(ratingId);
    }

    public RatingType findRatingByFilmId(Integer filmId) {
        Validator.validateId(filmId, "filmId");
        return findRatingById(filmId);
    }

}
