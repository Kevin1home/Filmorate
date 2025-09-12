package com.filmorate.storage.film;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.filmorate.excepsion.*;
import com.filmorate.excepsion.film.FilmAlreadyExistException;
import com.filmorate.excepsion.film.FilmNotFoundException;
import com.filmorate.excepsion.filmLike.FilmLikeAlreadyExistException;
import com.filmorate.excepsion.filmLike.FilmLikeNotFoundException;
import com.filmorate.excepsion.gender.GenreAlreadyExistException;
import com.filmorate.excepsion.gender.GenreNotFoundException;
import com.filmorate.excepsion.rating.RatingNotFoundException;
import com.filmorate.model.*;
import com.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.summingInt;

/**
 * In-memory implementation of {@link FilmStorage}.
 * <p>
 * Stores films, likes, genres, and ratings in local collections.
 * Intended for testing or prototyping purposes, not for production use.
 */
@Repository("inMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private final Set<FilmLike> filmLikes = new HashSet<>();
    private final Map<Integer, Genre> filmGenres = new HashMap<>();
    private final Map<Integer, RatingType> ratingTypes = new HashMap<>();

    private int filmNextId = 1;
    private int genreNextId = 1;
    private int ratingNextId = 1;

    @Autowired
    @Qualifier("inMemoryUserStorage")
    private UserStorage userStorage;

    @Override
    public List<Film> getAllFilms() {
        if (films.isEmpty()) {
            throw new FilmNotFoundException("No films available");
        }
        log.info("Retrieved {} films", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        return Optional.ofNullable(films.get(id))
                .orElseThrow(() -> new FilmNotFoundException("Film with ID " + id + " not found"));
    }

    @Override
    public Film addFilm(Film film) {
        boolean exists = films.values().stream()
                .anyMatch(f -> f.getName().equals(film.getName())
                        && f.getReleaseDate().equals(film.getReleaseDate()));
        if (exists) {
            throw new FilmAlreadyExistException("Film already exists");
        }

        film.setId(generateFilmId());
        films.put(film.getId(), film);
        log.info("Saved film: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(@NotNull @Valid Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Film with ID " + film.getId() + " not found");
        }
        films.put(film.getId(), film);

        log.info("Updated film: {}", film);
        return film;
    }

    @Override
    public List<FilmLike> getLikesByFilmId(int filmId) {
        getFilmById(filmId); // ensure film exists

        List<FilmLike> foundFilmLikes = filmLikes.stream()
                .filter(filmLike -> filmLike.getFilm().getId() == filmId)
                .toList();

        if (foundFilmLikes.isEmpty()) {
            throw new FilmLikeNotFoundException("No likes for film with ID " + filmId);
        }

        log.info("Retrieved {} likes for film {}", foundFilmLikes.size(), filmId);
        return foundFilmLikes;
    }

    @Override
    public Optional<FilmLike> getFilmLike(int filmId, int userId) {
        return filmLikes.stream()
                .filter(filmLike -> filmLike.getFilm().getId() == filmId && filmLike.getUser().getId() == userId)
                .findFirst();
    }

    @Override
    public FilmLike addLike(int filmId, int userId) {
        Film film = getFilmById(userId); // ensure film exists
        User user = userStorage.getUserById(userId); // ensure user exists

        if (getFilmLike(filmId, userId).isPresent()) {
            throw new FilmLikeAlreadyExistException(
                    String.format("User %s already liked film %s", userId, filmId));
        }
        FilmLike filmLike = new FilmLike(film, user);
        filmLikes.add(filmLike);

        log.info("Added like: {}", filmLike);
        return filmLike;
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        getFilmById(userId); // ensure film exists
        userStorage.getUserById(userId); // ensure user exists

        FilmLike filmLike = getFilmLike(filmId, userId)
                .orElseThrow(() -> new FilmLikeNotFoundException(
                        String.format("User %s has no like for film %s", userId, filmId)));

        filmLikes.remove(filmLike);
        log.info("Deleted like: {}", filmLike);
    }

    @Override
    public List<Film> findMostPopularFilms(int count) {
        Map<Film, Integer> mostPopularFilms = filmLikes.stream()
                .collect(Collectors.groupingBy(FilmLike::getFilm, summingInt(e -> 1)));

        log.info("Total liked films: {}", mostPopularFilms.size());

        return mostPopularFilms.entrySet()
                .stream()
                .sorted(Map.Entry.<Film, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(count)
                .toList();
    }

    @Override
    public List<GenreType> findAllGenres() {
        if (filmGenres.isEmpty()) {
            throw new GenreNotFoundException("No genres available");
        }
        log.info("Retrieved {} genres", filmGenres.size());
        return filmGenres.values().stream().map(Genre::getGenreType).toList();
    }

    @Override
    public GenreType findGenreById(int genreId) {
        return Optional.ofNullable(filmGenres.get(genreId))
                .map(Genre::getGenreType)
                .orElseThrow(() -> new GenreNotFoundException("Genre with ID " + genreId + " not found"));
    }

    @Override
    public List<GenreType> findGenreByFilmId(int filmId) {
        getFilmById(filmId); // ensure film exists

        List<GenreType> genres = filmGenres.values().stream()
                .filter(genre -> genre.getFilms().contains(films.get(filmId)))
                .map(Genre::getGenreType)
                .toList();

        log.info("Retrieved {} genres for film {}", genres.size(), filmId);
        return genres;
    }

    @Override
    public List<GenreType> addGenreToFilm(int filmId, List<String> genreList) {
        Film film = getFilmById(filmId);

        for (String genreString : genreList) {
            GenreType genreType = transformGenreNameToGenreType(genreString);

            Genre genre = filmGenres.values().stream()
                    .filter(g -> g.getGenreType().equals(genreType))
                    .toList().getLast();

            if (genre.getFilms().contains(film)) {
                throw new GenreAlreadyExistException("Genre already exists by this film");
            }
            genre.getFilms().add(film);
        }
        return findGenreByFilmId(filmId);
    }

    @Override
    public List<RatingType> findAllRatings() {
        return Arrays.asList(RatingType.values());
    }

    @Override
    public RatingType findRatingById(int ratingId) {
        return Optional.ofNullable(ratingTypes.get(ratingId))
                .orElseThrow(() -> new RatingNotFoundException("Rating with ID " + ratingId + " not found"));
    }

    @Override
    public RatingType findRatingByFilmId(int filmId) {
        return films.get(filmId).getRating();
    }

    public int generateFilmId() {
        return filmNextId++;
    }

    //-----------------------
    //Private helpers methods
    //-----------------------

    private int generateGenreId() {
        return genreNextId++;
    }

    private int generateRatingId() {
        return ratingNextId++;
    }

    /**
     * Converts a string representation of a genre to a {@link GenreType}.
     *
     * @param genreString the genre string, e.g., "comedy", "drama"
     * @return the corresponding {@link GenreType}
     * @throws NotFoundException if no match is found
     */
    private GenreType transformGenreNameToGenreType(String genreString) {
        return switch (genreString) {
            case ("comedy") -> GenreType.COMEDY;
            case ("drama") -> GenreType.DRAMA;
            case ("cartoon") -> GenreType.CARTOON;
            case ("thriller") -> GenreType.THRILLER;
            case ("documentary") -> GenreType.DOCUMENTARY;
            case ("action") -> GenreType.ACTION;
            default -> throw new NotFoundException("No such GenreType found");
        };
    }
}
