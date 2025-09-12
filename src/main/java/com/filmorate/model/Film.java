package com.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * Represents a film in the application.
 * <p>
 * Contains main metadata such as:
 * <ul>
 *   <li>id – unique identifier of the film</li>
 *   <li>name – title of the film</li>
 *   <li>description – short description (max 200 chars)</li>
 *   <li>rating – age rating (e.g. G, PG, R)</li>
 *   <li>releaseDate – official release date</li>
 *   <li>duration – duration in minutes (must be positive)</li>
 * </ul>
 */
@Data
public class Film {

    private int id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 200, message = "Description cannot be longer than 200 characters")
    private String description;

    @NotNull(message = "Rating cannot be null")
    private RatingType rating;

    @NotNull(message = "Release date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Positive(message = "Duration cannot be negative")
    private int duration;

    public Film(int id, String name, String description, RatingType rating, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
