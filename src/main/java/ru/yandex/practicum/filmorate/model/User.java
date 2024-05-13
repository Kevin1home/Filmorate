package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.ClockProvider;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Clock;
import java.time.LocalDate;

@Data
public class User {

    @Min(value = 1, message = "ID должно быть минимум 1")
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

/*    public User(@NonNull String email, @NonNull String login, String name, @NonNull LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }*/

/*    ClockProvider clockProvider = new ClockProvider() {
        @Override
        public Clock getClock() {
            return LocalDate.now();
        }
    };*/

}
