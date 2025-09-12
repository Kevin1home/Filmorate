package com.filmorate.storage.film;

import com.filmorate.model.Film;
import com.filmorate.model.FilmLike;
import com.filmorate.model.GenreType;
import com.filmorate.model.RatingType;

import java.util.List;
import java.util.Optional;

/**
 * Storage interface for managing {@link Film} entities and related data such as likes, genres, and ratings.
 * <p>
 * Defines the contract for all operations on films, including CRUD, likes management,
 * genre association, and rating retrieval.
 */
public interface FilmStorage {

    List<Film> getAllFilms();

    Film getFilmById(int id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<FilmLike> getLikesByFilmId(int filmId);

    Optional<FilmLike> getFilmLike(int filmId, int userId);

    FilmLike addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> findMostPopularFilms(int count);

    List<GenreType> findAllGenres();

    GenreType findGenreById(int genreId);

    List<GenreType> findGenreByFilmId(int genreId);

    List<GenreType> addGenreToFilm(int filmId, List<String> genreList);

    List<RatingType> findAllRatings();

    RatingType findRatingById(int ratingId);

    RatingType findRatingByFilmId(int filmId);
}
