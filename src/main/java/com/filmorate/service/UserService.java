package com.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.filmorate.excepsion.friendship.FriendshipNotFoundException;
import com.filmorate.service.util.Validator;
import com.filmorate.model.Friendship;
import com.filmorate.model.User;
import com.filmorate.storage.user.UserStorage;

import java.util.*;

/**
 * Service class for managing users and friendships.<br>
 * Handles user validation, friendships and common friends calculation.
 */
@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage")UserStorage userStorage) {
        this.userStorage = userStorage;
    }

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
            log.warn("Name is blank, using login '{}' as name", user.getLogin());
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
        Validator.validateId(friendId, "friendId");

        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        Optional<Friendship> friendIdUserId = userStorage.getFriendship(friendId, userId); // reverse friendship

        if (friendIdUserId.isPresent()) {
            Friendship updatedFriendship  = new Friendship(friendIdUserId.get().getId(), user, friend, true);
            userStorage.updateFriendship(updatedFriendship );
        }

        Friendship newFriendship = new Friendship(user, friend, true);
        return userStorage.addFriend(newFriendship);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        Validator.validateId(userId, "userId");
        Validator.validateId(friendId, "friendId");

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

        Set<User> friendsOfUser = new HashSet<>(userStorage.findFriends(userId));
        List<User> commonFriends = new ArrayList<>();

        for (User friend : userStorage.findFriends(otherId)) {
            if (friendsOfUser.contains(friend)) {
                commonFriends.add(friend);
            }
        }

        log.info("Total common friends between {} and {}: {}", userId, otherId, commonFriends.size());
        return commonFriends;
    }

    public List<User> findFriends(Integer userId) {
        Validator.validateId(userId, "userId");
        return userStorage.findFriends(userId);
    }
}
