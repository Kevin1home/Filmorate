package com.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

/**
 * Represents a user in the system.<br>
 * Each user has a unique ID, login, name, email, and birthday.
 */
@Data
public class User {

    private int id;

    @NotNull
    @NotBlank(message = "Login can not be empty")
    private String login;

    @NotNull
    private String name = "";

    @NotNull
    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Inappropriate email address format")
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Birthday date can not be in the future")
    private LocalDate birthday;

    public User(int id, String login, String name, String email, LocalDate birthday) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }
}
