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

    private Set<Integer> likes = new HashSet<>();

    @NonNull
    @NotBlank(message = "Название не может быть пустым.")
    private String name;

    @NonNull
    @NotBlank(message = "Описание не может быть пустым.")
    @Size(max = 200, message = "Длина описания не может быть больше 200 символов.")
    private String description;

    @NonNull
    @NotBlank(message = "Жанр не может быть пустым.")
    private Set<Genre> genre;

    @NonNull
    @NotBlank(message = "Рейтинг не может быть пустым.")
    private Rating rating;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность не может быть отрицательной.")
    private int duration;

}
