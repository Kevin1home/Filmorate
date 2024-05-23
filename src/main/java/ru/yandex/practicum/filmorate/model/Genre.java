package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Genre {

    private GenreType genreType;
    private Set<Film> films;

    public Genre(GenreType genreType, Set<Film> films) {
        this.genreType = genreType;
        this.films = films;
    }

}
