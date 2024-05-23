package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.excepsions.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.controller.Validator.validateFilm;

@Repository("inMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int filmNextId = 1;
    private final Set<FilmLike> filmLikes = new HashSet<>();
    private final Map<Integer, Genre> filmGenre = new HashMap<>();
    private int genreNextId = 1;
    private final Map<Integer, RatingType> ratingTypes = new HashMap<>();
    private int ratingNextId = 1;

    @Autowired
    @Qualifier("inMemoryUserStorage")
    private UserStorage userStorage;

    @Override
    public List<Film> getAllFilms() {
        if (films.isEmpty()) {
            throw new FilmNotFoundException("There are no films yet");
        }
        log.info("There are {} films found", films.size());
        return films.values().stream().toList();
    }

    @Override
    public Film getFilmById(int id) {
        if (films.isEmpty()) {
            throw new FilmNotFoundException("There are no films yet");
        }
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("No film under such ID found");
        }
        return films.get(id);
    }

    @Override
    public Film addFilm(Film film) {
        for (Film existingFilm : getAllFilms()) {
            if (existingFilm.getName().equals(film.getName())
                    || existingFilm.getReleaseDate().equals(film.getReleaseDate())) {
                throw new FilmAlreadyExistException("Film already exists");
            }
        }

        film.setId(generateFilmId());
        films.put(film.getId(), film);

        log.info("Saved film: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(@RequestBody @NotNull @Valid Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("No film under such ID found");
        }
        films.put(film.getId(), film);

        log.info("Updated film: {}", film);
        return film;
    }

    @Override
    public List<FilmLike> getLikesByFilmId(int filmId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException("No film under such ID found");
        }

        List<FilmLike> foundFilmLikes = new ArrayList<>();
        for (FilmLike filmLike : filmLikes) {
            if (filmLike.getFilm().getId() == filmId) {
                foundFilmLikes.add(filmLike);
            }
        }
        if (foundFilmLikes.isEmpty()) {
            throw new FilmLikeNotFoundException("There are no friends yet");
        }

        log.info("There are {} filmLikes found", foundFilmLikes.size());
        return foundFilmLikes;
    }

    @Override
    public Optional<FilmLike> getFilmLike(int filmId, int userId) {
        for (FilmLike filmLike : filmLikes) {
            if (filmLike.getFilm().getId() == filmId && filmLike.getUser().getId() == userId) {
                return Optional.of(filmLike);
            }
        }
        return Optional.empty();
    }

    @Override
    public FilmLike addLike(int filmId, int userId) {
        Film film = getFilmById(userId); // for checking whether exists
        User user = userStorage.getUserById(userId); // for checking whether exists

        if (getFilmLike(filmId, userId).isPresent()) {
            throw new FilmLikeAlreadyExistException(
                    String.format("User with ID %s already has added like to film with ID %s", userId, filmId));
        }
        FilmLike filmLike = new FilmLike(film, user);
        filmLikes.add(filmLike);

        log.info("Saved filmLike: {}", filmLike);
        return filmLike;
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        getFilmById(userId); // for checking whether exists
        userStorage.getUserById(userId); // for checking whether exists

        Optional<FilmLike> filmLike = getFilmLike(filmId, userId);
        if (filmLike.isEmpty()) {
            throw new FilmLikeNotFoundException(
                    String.format("User with ID %s has no like by film with ID %s", userId, filmId));
        }

        filmLikes.remove(filmLike.get());
        log.info("FilmLike was deleted");
    }

    @Override
    public List<Film> findMostPopularFilms(int count) {
        Map<Film, Integer> mostPopularFilms = new LinkedHashMap<>();

        for (FilmLike filmLike : filmLikes) {
            Film film = filmLike.getFilm();

            if (!mostPopularFilms.containsKey(film)) {
                mostPopularFilms.put(film, 1);
            }
            mostPopularFilms.put(film, mostPopularFilms.get(film) + 1);
        }

        log.info("Total liked films: {}", mostPopularFilms.size());

        return mostPopularFilms.entrySet()
                .stream()
                .sorted(Map.Entry.<Film, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public List<GenreType> findAllGenres() {
        if (filmGenre.isEmpty()) {
            throw new GenreNotFoundException("There are no genres yet");
        }
        log.info("There are {} genres found", filmGenre.size());
        return filmGenre.values().stream().map(Genre::getGenreType).toList();
    }

    @Override
    public GenreType findGenreById(int genreId) {
        if (filmGenre.isEmpty()) {
            throw new GenreNotFoundException("There are no genres yet");
        }
        if (!filmGenre.containsKey(genreId)) {
            throw new GenreNotFoundException("No genre under such ID found");
        }
        return filmGenre.get(genreId).getGenreType();
    }

    @Override
    public List<GenreType> findGenreByFilmId(int filmId) {
        if (filmGenre.isEmpty()) {
            throw new GenreNotFoundException("There are no genres yet");
        }
        List<GenreType> genres = filmGenre.values().stream()
                .filter(genre -> films.containsKey(filmId))
                .map(Genre::getGenreType)
                .collect(Collectors.toList());

        log.info("There are {} genres found", genres.size());
        return genres;
    }

    @Override
    public List<GenreType> addGenreToFilm(int filmId, List<String> genreList) {
        Film film = getFilmById(filmId);

        for (String genreString : genreList) {
            GenreType genreType = transformGenreNameToGenreType(genreString); // checking list with genre for wrong writing

            Genre genre = filmGenre.values().stream()
                    .filter(g -> g.getGenreType().equals(genreType))
                    .toList().getLast();

            if (genre.getFilms().contains(film)) {
                throw new GenreAlreadyExistException("Such genre already exists by this film");
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
        return ratingTypes.get(ratingId);
    }

    @Override
    public RatingType findRatingByFilmId(int filmId) {
        return films.get(filmId).getRating();
    }

    public int generateFilmId() {
        return filmNextId++;
    }

    public int generateGenreId() {
        return genreNextId++;
    }

    public int generateRatingId() {
        return ratingNextId++;
    }

    private GenreType transformGenreNameToGenreType(String genreString) {
        switch (genreString) {
            case ("comedy"):
                return GenreType.COMEDY;
            case ("drama"):
                return GenreType.DRAMA;
            case ("cartoon"):
                return GenreType.CARTOON;
            case ("thriller"):
                return GenreType.THRILLER;
            case ("documentory"):
                return GenreType.DOCUMENTORY;
            case ("action"):
                return GenreType.ACTION;
            default:
                throw new NotFoundException("No such GenreType found");
        }
    }

}
