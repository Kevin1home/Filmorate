package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.excepsions.ValidationException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Received GET request: getAllUsers");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(required = false, value = "id") Integer userId) {
        log.info("Received GET request: getUserById");
        return userService.getUserById(userId);
    }

    @PostMapping
    public User addUser(@RequestBody @NotNull @Valid User user) {
        log.info("Received POST request: addUser");
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @NotNull @Valid User user) {
        log.info("Received PUT request: updateUser");
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Friendship addFriend(@PathVariable(required = false, value = "id") Integer userId,
                                @PathVariable(required = false) Integer friendId) {
        log.info("Received POST request: addFriend");
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseTransfer deleteFriend(@PathVariable(required = false) Integer id,
                             @PathVariable(required = false) Integer friendId) {
        log.info("Received DELETE request: deleteFriend");
        userService.deleteFriend(id, friendId);
        return new ResponseTransfer("Friend deleted");
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable(required = false, value = "id") Integer userId,
                                        @PathVariable(required = false) Integer otherId) {
        log.info("Received GET request: findCommonFriends");
        return userService.findCommonFriends(userId, otherId);
    }

    @GetMapping("{id}/friends")
    public List<User> findFriends(@PathVariable(required = false, value = "id") Integer userId) {
        log.info("Received GET request: findFriends");
        return userService.findFriends(userId);
    }

}
