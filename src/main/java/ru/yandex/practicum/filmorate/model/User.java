package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.*;

/**
 * User.
 */
@Data
public class User {

    private int id;

    @NonNull
    @NotBlank(message = "Login can not be empty")
    private String login;

    private String name = "";

    @NonNull
    @Email(message = "Inappropriate email address format")
    @NotBlank(message = "Email address cannot be empty")
    private String email;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Birthday date can not be in the future")
    private LocalDate birthday;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(login, user.login) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(birthday, user.birthday);
    }

    /*@Override
    public int hashCode() {
        return Objects.hash(id, login, name, email, birthday);
    }*/
}
