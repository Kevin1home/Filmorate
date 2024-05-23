package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.excepsions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.GenreType;
import ru.yandex.practicum.filmorate.model.RatingType;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
