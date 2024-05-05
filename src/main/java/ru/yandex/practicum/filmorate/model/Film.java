package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    final private int id = hashCode();
    final private String name;
    final private String description;
    final private LocalDate releaseDate;
    final private Duration duration;

}
