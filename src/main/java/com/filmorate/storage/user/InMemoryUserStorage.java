package com.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import com.filmorate.excepsion.friendship.FriendshipAlreadyExistException;
import com.filmorate.excepsion.friendship.FriendshipNotFoundException;
import com.filmorate.excepsion.user.UserAlreadyExistException;
import com.filmorate.excepsion.user.UserNotFoundException;
import com.filmorate.model.Friendship;
import com.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * In-memory implementation of {@link UserStorage}.
 * <p>
 * Stores users and friendships in HashMaps.
 * Intended for testing and prototyping purposes.
 */
@Repository("inMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();
    private final HashMap<Integer, Friendship> friendships = new HashMap<>();

    private int nextUserId = 1;
    private int nextFriendshipId = 1;

    @Override
    public List<User> getAllUsers() {
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users available");
        }
        log.info("Retrieved {} users", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }

    @Override
    public User addUser(User user) {
        boolean exists = users.values().stream()
                .anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()));

        if (exists) {
            throw new UserAlreadyExistException("User with email " + user.getEmail() + " already exists");
        }

        user.setId(generateUserId());
        users.put(user.getId(), user);

        log.info("Added user: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("User with ID " + user.getId() + " not found");
        }
        users.put(user.getId(), user);

        log.info("Updated user: {}", user);
        return user;
    }

    @Override
    public Friendship addFriend(Friendship friendship) {
        int userId = friendship.getUser().getId();
        int friendId = friendship.getFriend().getId();

        if (getFriendship(userId, friendId).isPresent()) {
            throw new FriendshipAlreadyExistException(
                    String.format("User %s already has user %s as friend", userId, friendId));
        }

        friendship.setId(generateFriendshipId());
        friendships.put(friendship.getId(), friendship);

        log.info("Added friendship: {}", friendship);
        return friendship;
    }
    @Override
    public void updateFriendship(Friendship friendship) {
        friendships.put(friendship.getId(), friendship);
        log.info("Updated friendship: {}", friendship);
    }

    @Override
    public Optional<Friendship> getFriendship(int userId, int friendId) {
        return friendships.values().stream()
                .filter(f -> f.getUser().getId() == userId && f.getFriend().getId() == friendId)
                .findFirst();
    }

    @Override
    public void deleteFriend(Friendship friendship) {
        friendships.remove(friendship.getId());
        log.info("Deleted friendship: {}", friendship);
    }

    @Override
    public List<User> findFriends(int userId) {
        getUserById(userId); // ensure user exists

        List<User> friends = friendships.values().stream()
                .filter(f -> f.getUser().getId() == userId)
                .map(Friendship::getFriend)
                .collect(Collectors.toList());

        if (friends.isEmpty()) {
            throw new FriendshipNotFoundException("User with ID " + userId + " has no friends");
        }

        log.info("Retrieved {} friends for user {}", friends.size(), userId);
        return friends;
    }

    //-----------------------
    //Private helpers methods
    //-----------------------

    private int generateUserId() {
        return nextUserId++;
    }

    private int generateFriendshipId() {
        return nextFriendshipId++;
    }
}
