package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {

    @Min(value = 1, message = "ID должно быть минимум 1.")
    private int id;

    @NonNull
    @NotBlank(message = "Название не может быть пустым.")
    private String name;

    @NonNull
    @NotBlank(message = "Описание не может быть пустым.")
    @Size(max = 200, message = "Длина описания не может быть больше 200 символов.")
    private String description;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность не может быть отрицательной.")
    final private int duration;

}
