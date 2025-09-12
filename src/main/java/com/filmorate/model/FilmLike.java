package com.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a "like" relation between a {@link User} and a {@link Film}.
 * <p>
 * Each instance means that a specific user liked a specific film.
 */
@Getter
@ToString
@EqualsAndHashCode
public class FilmLike {

    private final Film film;
    private final User user;

    public FilmLike(Film film, User user) {
        this.film = film;
        this.user = user;
    }
}
