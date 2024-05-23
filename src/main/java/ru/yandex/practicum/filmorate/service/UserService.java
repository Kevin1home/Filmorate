package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.Validator;
import ru.yandex.practicum.filmorate.excepsions.*;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Slf4j
@Service
public class UserService {

    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer userId) {
        Validator.validateId(userId, "userId");
        return userStorage.getUserById(userId);
    }

    public User addUser(User user) {
        Validator.validateUser(user);

        if (user.getName().isBlank()) {
            log.warn("The name is empty, so the login is used as name!");
            user.setName(user.getLogin());
        }

        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        Validator.validateUser(user);
        return userStorage.updateUser(user);
    }

    public Friendship addFriend(Integer userId, Integer friendId) {
        Validator.validateId(userId, "userId");
        Validator.validateId(userId, "friendId");

        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        Optional<Friendship> friendIdUserId = userStorage.getFriendship(friendId, userId); // reverse friendship

        if (friendIdUserId.isPresent()) { // and userIdFriendId is Empty
            Friendship friendshipFriend = new Friendship(friendIdUserId.get().getId(), user, friend, true);
            userStorage.updateFriendship(friendshipFriend); // change existing friendship
        }

        Friendship friendshipUser = new Friendship(user, friend, true);
        return userStorage.addFriend(friendshipUser); // create new friendship
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        Validator.validateId(userId, "userId");
        Validator.validateId(userId, "friendId");

        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        Optional<Friendship> userIdFriendId = userStorage.getFriendship(userId, friendId);
        Optional<Friendship> friendIdUserId = userStorage.getFriendship(friendId, userId); // reverse friendship

        if (userIdFriendId.isEmpty()) {
            log.info("User with ID {} does not contain user with ID {} in friends", userId, friendId);
            throw new FriendshipNotFoundException(
                    String.format("User with ID %s has not a user with ID %s as friend", userId, friendId));
        }
        if (friendIdUserId.isPresent()) { // and userIdFriendId is Present
            Friendship friendshipFriend = new Friendship(friendIdUserId.get().getId(), user, friend, false);
            userStorage.updateFriendship(friendshipFriend); // change existing friendship by Friend
        }

        userStorage.deleteFriend(userIdFriendId.get()); // delete friendship by User
    }

    public List<User> findCommonFriends(Integer userId, Integer otherId) {
        Validator.validateId(userId, "userId");
        Validator.validateId(otherId, "otherId");

        List<User> commonFriends = new ArrayList<>();
        for (User friend : userStorage.findFriends(userId)) {
            if (userStorage.findFriends(otherId).contains(friend)) {
                commonFriends.add(friend);
            }
        }

        log.info("Total common friends: {}", commonFriends.size());
        return commonFriends;
    }

    public List<User> findFriends(Integer userId) {
        Validator.validateId(userId, "userId");
        return userStorage.findFriends(userId);
    }

}
