package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepsions.NotFoundException;
import ru.yandex.practicum.filmorate.excepsions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getUserStorage().getAllUsers().values().stream().toList();
    }

    @PostMapping
    public User addUser(@RequestBody @NotNull @Valid User user) throws ValidationException {
        return userService.getUserStorage().addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @NotNull @Valid User user) throws ValidationException {
        return userService.getUserStorage().updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable(required = false) Integer id,
                          @PathVariable(required = false) Integer friendId) {
        if (id == null || id <= 0) {
            throw new NotFoundException("userId");
        }
        if (friendId == null || friendId <= 0) {
            throw new NotFoundException("friendId");
        }
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable(required = false) Integer id,
                             @PathVariable(required = false) Integer friendId) {
        if (id == null || id <= 0) {
            throw new NotFoundException("userId");
        }
        if (friendId == null || friendId <= 0) {
            throw new NotFoundException("friendId");
        }
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable(required = false) Integer id,
                                           @PathVariable(required = false) Integer otherId) {
        if (id == null || id <= 0) {
            throw new NotFoundException("userId");
        }
        if (otherId == null || otherId <= 0) {
            throw new NotFoundException("otherId");
        }
        return userService.findCommonFriends(id, otherId);
    }

    @GetMapping("{id}/friends")
    public List<Integer> findFriends(@PathVariable(required = false) Integer id) {
        if (id == null || id <= 0) {
            throw new NotFoundException("userId");
        }
        return userService.findFriends(id);
    }

}
