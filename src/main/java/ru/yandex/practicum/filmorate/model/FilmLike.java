package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FilmLike {

    private Film film;
    private User user;

    public FilmLike(Film film, User user) {
        this.film = film;
        this.user = user;
    }

}
