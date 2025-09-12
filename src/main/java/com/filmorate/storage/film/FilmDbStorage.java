package com.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.filmorate.excepsion.*;
import com.filmorate.excepsion.film.FilmAlreadyExistException;
import com.filmorate.excepsion.film.FilmNotFoundException;
import com.filmorate.excepsion.filmLike.FilmLikeAlreadyExistException;
import com.filmorate.excepsion.filmLike.FilmLikeNotFoundException;
import com.filmorate.excepsion.friendship.FriendshipNotFoundException;
import com.filmorate.excepsion.gender.GenreNotFoundException;
import com.filmorate.excepsion.rating.RatingNotFoundException;
import com.filmorate.model.*;
import com.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of {@link FilmStorage} that works with a relational database using {@link JdbcTemplate}.
 * <p>
 * Responsible for CRUD operations with films, likes, genres, and ratings.
 * Performs SQL queries to retrieve and modify data in tables:
 * <ul>
 *     <li>film</li>
 *     <li>film_like</li>
 *     <li>genre</li>
 *     <li>film_genre</li>
 *     <li>rating</li>
 * </ul>
 */
@Slf4j
@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT f.name, f.description, r.name, f.release_date, f.duration " +
                "FROM film AS f " +
                "LEFT OUTER JOIN rating AS r ON f.rating_id=r.id " +
                "ORDER BY f.name";
        List<Film> allFilms = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));

        log.info("There are {} films found", allFilms.size());
        return allFilms;
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "SELECT f.name, f.description, r.name, f.release_date, f.duration " +
                "FROM film AS f " +
                "LEFT OUTER JOIN rating AS r ON f.rating_id=r.id " +
                "WHERE f.id=?";
        List<Film> filmList = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id);

        if (filmList.isEmpty()) {
            throw new FilmNotFoundException("No film under such ID found");
        }
        return filmList.getLast();
    }

    @Override
    public Film addFilm(Film film) {
        for (Film existingFilm : getAllFilms()) {
            if (existingFilm.getName().equals(film.getName())
                    && existingFilm.getReleaseDate().equals(film.getReleaseDate())) {
                throw new FilmAlreadyExistException("Film already exists");
            }
        }

        String sql = "INSERT INTO film (name, description, rating_id, release_date, duration) " +
                     "VALUES (?, ?, ?, ?, ?)";
        String name = film.getName();
        String description = film.getDescription();
        int ratingId = findIdByRatingType(film.getRating());
        Date releaseDate = Date.valueOf(film.getReleaseDate());
        int duration = film.getDuration();

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int qnt = jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setInt(3, ratingId);
            stmt.setDate(4, releaseDate);
            stmt.setInt(5, duration);
            return stmt;
        }, keyHolder);
        log.info("Added {} new records to table film", qnt);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(id);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        getFilmById(film.getId()); // for checking whether exists

        String sql = "UPDATE film " +
                     "SET name=?, description=?, rating_id=?, release_date=?, duration=? " +
                     "WHERE id=?";
        String name = film.getName();
        String description = film.getDescription();
        int ratingId = findIdByRatingType(film.getRating());
        Date releaseDate = Date.valueOf(film.getReleaseDate());
        int duration = film.getDuration();
        int id = film.getId();

        int qnt = jdbcTemplate.update(sql, name, description, ratingId, releaseDate, duration, id);
        log.info("Updated {} records by table film", qnt);

        return film;
    }

    @Override
    public List<FilmLike> getLikesByFilmId(int filmId) {
        getFilmById(filmId); // for checking whether exists

        String sql = "SELECT * FROM film_like WHERE film_id=?";
        List<FilmLike> allFilmLikes = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilmLike(rs), filmId);

        if (allFilmLikes.isEmpty()) {
            throw new FilmLikeNotFoundException("There are no filmLikes yet");
        }
        log.info("There are {} filmLikes found", allFilmLikes.size());
        return allFilmLikes;
    }

    @Override
    public Optional<FilmLike> getFilmLike(int filmId, int userId) {
        String sql = "SELECT * FROM film_like WHERE film_id=? AND user_id=?";
        List<FilmLike> filmLikeList = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilmLike(rs), filmId, userId);

        if (filmLikeList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(filmLikeList.getLast());
    }

    @Override
    public FilmLike addLike(int filmId, int userId) {
        getFilmById(filmId); // for checking whether exists
        userStorage.getUserById(userId); // for checking whether exists

        if (getFilmLike(filmId, userId).isPresent()) {
            throw new FilmLikeAlreadyExistException(
                    String.format("User with ID %s already has added like to film with ID %s", userId, filmId));
        }

        String sql = "INSERT INTO film_like (film_id, user_id) " +
                     "VALUES (?, ?)";

        int qnt = jdbcTemplate.update(sql, filmId, userId);
        log.info("Added {} new records to table film_like", qnt);

        Optional<FilmLike> newFilmLike = getFilmLike(filmId, userId);
        if (newFilmLike.isEmpty()) {
            log.error("FilmLike from user {} to film {} not found after adding to DB!", userId, filmId);
            throw new FriendshipNotFoundException(
                    String.format("Error by adding new FilmLike from user %s to film %s", userId, filmId));
        }
        return newFilmLike.get();
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        getFilmById(filmId); // for checking whether exists
        userStorage.getUserById(userId); // for checking whether exists

        if (getFilmLike(filmId, userId).isEmpty()) {
            throw new FilmLikeNotFoundException(
                    String.format("User with ID %s has no like by film with ID %s", userId, filmId));
        }

        String sql = "DELETE FROM film_like WHERE film_id=? AND user_id=?";

        int qnt = jdbcTemplate.update(sql, filmId, userId);
        log.info("Deleted {} records from table friendship", qnt);
    }

    @Override
    public List<Film> findMostPopularFilms(int count) {
        String sql = "SELECT f.name, f.description, r.name, f.release_date, f.duration, COUNT(fl.film_id) AS likes " +
                     "FROM film AS f " +
                     "LEFT OUTER JOIN rating AS r ON f.rating_id=r.id " +
                     "LEFT OUTER JOIN film_like AS fl ON f.id=fl.film_id " +
                     "GROUP BY f.id " +
                     "ORDER BY likes DESC " +
                     "LIMIT ?";
        List<Film> allFilms = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);

        if (allFilms.isEmpty()) {
            throw new FilmNotFoundException("There are no films yet");
        }
        log.info("There are {} films found", allFilms.size());
        return allFilms;
    }

    @Override
    public List<GenreType> findAllGenres() {
        String sql = "SELECT name " +
                     "FROM genre " +
                     "ORDER BY name";
        List<String> allGenresString = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"));
        if (allGenresString.isEmpty()) {
            throw new GenreNotFoundException("There are no genres yet");
        }

        List<GenreType> allGenres = allGenresString.stream()
                .map(this::transformGenreNameToGenreType)
                .toList();

        log.info("There are {} genres found", allGenres.size());
        return allGenres;
    }

    @Override
    public GenreType findGenreById(int genreId) {
        String sql = "SELECT name " +
                     "FROM genre " +
                     "WHERE id=?";
        List<String> genreListString = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"), genreId);

        if (genreListString.isEmpty()) {
            throw new GenreNotFoundException("No genres under ID " + genreId + " found");
        }

        List<GenreType> genreList = genreListString.stream()
                .map(this::transformGenreNameToGenreType)
                .toList();

        return genreList.getLast();
    }

    @Override
    public List<GenreType> findGenreByFilmId(int filmId) {
        String sql = "SELECT g.name " +
                     "FROM genre AS g " +
                     "RIGHT OUTER JOIN film_genre AS fg ON g.id=fg.genre_id " +
                     "WHERE fg.film_id=? " +
                     "ORDER BY g.name";
        List<String> genresString = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"), filmId);
        if (genresString.isEmpty()) {
            throw new GenreNotFoundException("There are no genres by film ID " + filmId);
        }

        List<GenreType> genres = genresString.stream()
                .map(this::transformGenreNameToGenreType)
                .toList();

        log.info("There are {} genres found", genres.size());
        return genres;
    }

    @Override
    public List<GenreType> addGenreToFilm(int filmId, List<String> genreList) {

        for (String genreString : genreList) {
            GenreType genre = transformGenreNameToGenreType(genreString); // checking list with genre for wrong writing
            int genre_id = findIdByGenreType(genre);

            String sql = "INSERT INTO film_genre (film_id, genre_id) " +
                         "VALUES (?, ?)";
            int qnt = jdbcTemplate.update(sql, filmId, genre_id);
            log.info("Added {} new records to table film_like", qnt);
        }
        return findGenreByFilmId(filmId);
    }

    @Override
    public List<RatingType> findAllRatings() {
        String sql = "SELECT name " +
                     "FROM rating " +
                     "ORDER BY name";
        List<String> allRatingsString = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"));
        if (allRatingsString.isEmpty()) {
            throw new RatingNotFoundException("There are no ratings yet");
        }

        List<RatingType> allRatings = allRatingsString.stream()
                .map(this::transformRatingNameToRatingType)
                .toList();

        log.info("There are {} ratings found", allRatings.size());
        return allRatings;
    }

    @Override
    public RatingType findRatingById(int ratingId) {
        String sql = "SELECT name " +
                     "FROM rating " +
                     "WHERE id=?";
        List<String> ratingListString = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"), ratingId);

        if (ratingListString.isEmpty()) {
            throw new RatingNotFoundException("No ratings under ID " + ratingId + " found");
        }

        List<RatingType> ratingList = ratingListString.stream()
                .map(this::transformRatingNameToRatingType)
                .toList();

        return ratingList.getLast();
    }

    @Override
    public RatingType findRatingByFilmId(int filmId) {
        String sql = "SELECT r.name AS name " +
                     "FROM rating AS r " +
                     "INNER JOIN film AS f ON r.id=f.rating_id " +
                     "WHERE f.id=?";
        List<String> ratingStringList = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"), filmId);
        if (ratingStringList.isEmpty()) {
            throw new RatingNotFoundException("There are no rating by film ID " + filmId);
        }
        String ratingString = ratingStringList.getLast();
        RatingType rating = transformRatingNameToRatingType(ratingString);

        log.info("Film with ID {} has rating {}", filmId, rating);
        return rating;
    }

    //-----------------------
    //Private helpers methods
    //-----------------------

    /**
     * Maps a single row from {@link ResultSet} to a {@link FilmLike} object.
     *
     * @param rs ResultSet containing data from the film_like table
     * @return a FilmLike object (film + user)
     * @throws SQLException if an error occurs while reading the ResultSet
     */
    private FilmLike makeFilmLike(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        int filmId = rs.getInt("film_id");

        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        return new FilmLike(film, user);
    }

    /**
     * Maps a single row from {@link ResultSet} to a {@link Film} object.
     *
     * @param rs ResultSet containing data from the film table
     * @return a Film object
     * @throws SQLException if an error occurs while reading the ResultSet
     */
    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        String ratingName = rs.getString("rating_name");
        RatingType rating = transformRatingNameToRatingType(ratingName);
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");

        return new Film(id, name, description, rating, releaseDate, duration);
    }

    /**
     * Finds the ID of a rating in the database by its enum value.
     *
     * @param ratingType value of {@link RatingType}
     * @return numeric ID of the rating from the database
     * @throws NotFoundException if the rating does not exist in the database
     */
    private int findIdByRatingType(RatingType ratingType) {
        String ratingName = transformRatingTypeToRatingName(ratingType);

        String sql = "SELECT * FROM rating WHERE name=?";
        List<Integer> ratingId = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("id"), ratingName);

        if (ratingId.isEmpty()) {
            throw new NotFoundException("No ratingName under such ID in DB found");
        }
        return ratingId.getLast();
    }

    /**
     * Finds the ID of a genre in the database by its enum value.
     *
     * @param genreType value of {@link GenreType}
     * @return numeric ID of the genre from the database
     * @throws NotFoundException if the genre does not exist in the database
     */
    private int findIdByGenreType(GenreType genreType) {
        String genreName = transformGenreTypeToGenreName(genreType);

        String sql = "SELECT * FROM genre WHERE name=?";
        List<Integer> genreId = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("id"), genreName);

        if (genreId.isEmpty()) {
            throw new NotFoundException("No genreName under such ID in DB found");
        }
        return genreId.getLast();
    }

    /**
     * Converts a genre name from the database into {@link GenreType}.
     *
     * @param genreString string value of genre from the genre table
     * @return corresponding enum value of GenreType
     * @throws NotFoundException if the genre name is not recognized
     */
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
            case ("documentary"):
                return GenreType.DOCUMENTARY;
            case ("action"):
                return GenreType.ACTION;
            default:
                throw new NotFoundException("No such GenreType found");
        }
    }

    /**
     * Converts a {@link GenreType} enum into its string representation for database storage.
     *
     * @param genreType enum value of genre
     * @return string representation of genre (e.g., "comedy")
     * @throws NotFoundException if the genre is not recognized
     */
    private String transformGenreTypeToGenreName(GenreType genreType) {
        switch (genreType) {
            case COMEDY:
                return "comedy";
            case DRAMA:
                return "drama";
            case CARTOON:
                return "cartoon";
            case THRILLER:
                return "thriller";
            case DOCUMENTARY:
                return "documentary";
            case ACTION:
                return "action";
            default:
                throw new NotFoundException("No such GenreType found");
        }
    }

    /**
     * Converts a rating name from the database into {@link RatingType}.
     *
     * @param ratingString string value of rating (e.g., "PG-13")
     * @return corresponding enum value of RatingType
     * @throws NotFoundException if the rating name is not recognized
     */
    private RatingType transformRatingNameToRatingType(String ratingString) {
        switch (ratingString) {
            case ("G"):
                return RatingType.G;
            case ("PG"):
                return RatingType.PG;
            case ("PG-13"):
                return RatingType.PG_13;
            case ("R"):
                return RatingType.R;
            case ("NC-17"):
                return RatingType.NC_17;
            default:
                throw new NotFoundException("No such RatingType found");
        }
    }

    /**
     * Converts a {@link RatingType} enum into its string representation for database storage.
     *
     * @param ratingType enum value of rating
     * @return string representation of rating (e.g., "R")
     * @throws NotFoundException if the rating is not recognized
     */
    private String transformRatingTypeToRatingName(RatingType ratingType) {
        switch (ratingType) {
            case G:
                return "G";
            case PG:
                return "PG";
            case PG_13:
                return "PG-13";
            case R:
                return "R";
            case NC_17:
                return "NC-17";
            default:
                throw new NotFoundException("No such RatingType in RatingType");
        }
    }
}
