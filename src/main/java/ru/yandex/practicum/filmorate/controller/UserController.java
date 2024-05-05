package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepsions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users.values().stream().toList();
    }

    @PostMapping
    public User addUser(@RequestBody @NonNull User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("ValidationException! Некорректный майл!");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.",
                    user.getEmail());
        } else if (user.getLogin().isBlank()) {
            log.warn("ValidationException! Логин пустой или содержит пробелы!");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.", user.getLogin());
        } else if (user.getName().isBlank()) {
            log.warn("Имя пустое, поэтому используется логин!");
            user.setName(user.getLogin());
        }else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("ValidationException! Дата рождения указана в будущем!");
            throw new ValidationException("Дата рождения не может быть в будущем.",
                    String.valueOf(user.getBirthday()));
        } else {
            users.put(user.getId(), user);
        }
        log.info("Сохранённый пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User addOrUpdateUser(@RequestBody @NonNull User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("ValidationException! Некорректный майл!");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.",
                    user.getEmail());
        } else if (user.getLogin().isBlank()) {
            log.warn("ValidationException! Логин пустой или содержит пробелы!");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.", user.getLogin());
        } else if (user.getName().isBlank()) {
            log.warn("Имя пустое, поэтому используется логин!");
            user.setName(user.getLogin());
        }else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("ValidationException! Дата рождения указана в будущем!");
            throw new ValidationException("Дата рождения не может быть в будущем.",
                    String.valueOf(user.getBirthday()));
        } else {
            users.put(user.getId(), user);
        }
        log.info("Сохранённый пользователь: {}", user);
        return user;
    }

}
