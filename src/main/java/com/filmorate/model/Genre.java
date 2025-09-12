package com.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a genre of films.<br>
 * Each instance contains a genre type and a set of films that belong to this genre.
 */
// TODO: to implement methods
@Getter
@Setter
public class Genre {

    private final GenreType genreType;
    private final Set<Film> films;

    public Genre(GenreType genreType, Set<Film> films) {
        this.genreType = genreType;
        this.films = (films != null) ? films : new HashSet<>();
    }

    /**
     * Adds a film to this genre.
     *
     * @param film the film to add
     * @return true if the film was added, false if it was already present
     */
    public boolean addFilm(Film film) {
        return films.add(film);
    }

    /**
     * Removes a film from this genre.
     *
     * @param film the film to remove
     * @return true if the film was removed, false if it was not present
     */
    public boolean removeFilm(Film film) {
        return films.remove(film);
    }
}
