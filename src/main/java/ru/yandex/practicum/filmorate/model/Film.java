package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {

    private int id;

    @NonNull
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NonNull
    @NotBlank(message = "Description cannot be empty")
    @Size(max = 200, message = "Description cannot be longer than 200 characters")
    private String description;

    @NonNull
    @NotBlank(message = "Rating cannot be empty")
    private RatingType rating;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Positive(message = "Duration cannot be negative")
    private int duration;

}
