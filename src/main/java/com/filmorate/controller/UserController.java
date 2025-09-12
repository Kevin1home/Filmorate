package com.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.filmorate.controller.util.ResponseTransfer;
import com.filmorate.model.Friendship;
import com.filmorate.model.User;
import com.filmorate.service.UserService;

import java.util.List;

/**
 * REST controller for managing users and friends.
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("GET /users — fetching all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") Integer userId) {
        log.info("GET /users/{} — fetching user by ID", userId);
        return userService.getUserById(userId);
    }

    @PostMapping
    public User addUser(@RequestBody @NotNull @Valid User user) {
        log.info("POST /users — adding user: {}", user.getName());
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @NotNull @Valid User user) {
        log.info("PUT /users — updating user ID: {}", user.getId());
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Friendship addFriend(@PathVariable(value = "id") Integer userId,
                                @PathVariable Integer friendId) {
        log.info("PUT /{}/friends/{} — adding user: {} new friend: {}", userId, friendId, userId, friendId);
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseTransfer deleteFriend(@PathVariable(value = "id") Integer userId,
                                         @PathVariable Integer friendId) {
        log.info("DELETE {}/friends/{} — removing friend: {} by user: {}", userId, friendId, friendId, userId);
        userService.deleteFriend(userId, friendId);
        return new ResponseTransfer("Friend deleted");
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable(value = "id") Integer userId,
                                        @PathVariable Integer otherId) {
        log.info("GET {}/friends/common/{} — fetching all common friends between " +
                "user: {} and other user: {}", userId, otherId, userId, otherId);
        return userService.findCommonFriends(userId, otherId);
    }

    @GetMapping("{id}/friends")
    public List<User> findFriends(@PathVariable(value = "id") Integer userId) {
        log.info("GET {}/friends — fetching all friends of user: {}", userId, userId);
        return userService.findFriends(userId);
    }
}
