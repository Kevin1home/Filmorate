package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepsions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.controller.Validator.validateUser;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @GetMapping
    public List<User> getAllUsers() {

        log.info("Текущее количество пользователей: {}", users.size());
        return users.values().stream().toList();
    }

    @PostMapping
    public User addUser(@RequestBody @NotNull @Valid User user) throws ValidationException {
        if (!validateUser(user)) {
            throw new ValidationException("Валидация параметров пользователя не пройдена.",
                    String.valueOf(user));
        }

        for (User userExisting : users.values()) {
            boolean isEqual = Objects.equals(userExisting.getEmail(), user.getEmail());
            if (isEqual) {
                throw new ValidationException("Такой пользователь уже есть.",
                        String.valueOf(user));
            }
        }

        if (user.getName().isBlank()) {
            log.warn("Имя пустое, поэтому используется логин!");
            user.setName(user.getLogin());
        }

        user.setId(generateId());
        users.put(user.getId(), user);

        log.info("Сохранённый пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @NotNull @Valid User user) throws ValidationException {
        if (!validateUser(user)) {
            throw new ValidationException("Валидация параметров пользователя не пройдена.",
                    String.valueOf(user));
        }

        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Такого id нет.", String.valueOf(user.getId()));
        }
        users.put(user.getId(), user);

        log.info("Обновлённый пользователь: {}", user);
        return user;
    }

    public int generateId() {
        return nextId++;
    }

}
