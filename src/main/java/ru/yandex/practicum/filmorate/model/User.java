package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class User {

    private int id;

    @NonNull
    @Email(message = "Несоотвествующий формат адреса электронной почты.")
    @NotBlank(message = "Адрес электронной почты не может быть пустым")
    private String email;

    @NonNull
    @NotBlank(message = "Логин не может быть пустым.")
    private String login;

    private String name = "";

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Дата дня рождения не может быть в будущем.")
    private LocalDate birthday;

}
