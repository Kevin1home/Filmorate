package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.excepsions.FriendshipAlreadyExistException;
import ru.yandex.practicum.filmorate.excepsions.FriendshipNotFoundException;
import ru.yandex.practicum.filmorate.excepsions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.excepsions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository("inMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int nextUserId = 1;
    private final HashMap<Integer, Friendship> friendships = new HashMap<>();
    private int nextFriendshipId = 1;

    @Override
    public List<User> getAllUsers() {
        if (users.isEmpty()) {
            throw new UserNotFoundException("There are no users yet");
        }
        log.info("There are {} users found", users.size());
        return users.values().stream().toList();
    }

    @Override
    public User getUserById(int id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("No user under such ID found");
        }
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        for (User existingUser : getAllUsers()) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                throw new UserAlreadyExistException("User already exists");
            }
        }

        user.setId(generateUserId());
        users.put(user.getId(), user);

        log.info("Saved user: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("No user under such ID found");
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
                    String.format("User with ID %s already has a user with ID %s as friend", userId, friendId));
        }

        friendship.setId(generateFriendshipId());
        friendships.put(friendship.getId(), friendship);

        log.info("Saved friendship: {}", friendship);
        return friendship;
    }

    @Override
    public void updateFriendship(Friendship friendship) {
        friendships.put(friendship.getId(), friendship);
        log.info("Updated friendship: {}", friendship);
    }

    @Override
    public Optional<Friendship> getFriendship(int userId, int friendId) {
        for (Friendship friendship : friendships.values()) {
            if (friendship.getUser().getId() == userId && friendship.getFriend().getId() == friendId) {
                return Optional.of(friendship);
            }
        }
        return Optional.empty();
    }

    @Override
    public void deleteFriend(Friendship friendship) {
        friendships.remove(friendship.getId());
        log.info("Friendship was deleted");
    }

    @Override
    public List<User> findFriends(int userId) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException("No user under such ID found");
        }

        List<User> friends = new ArrayList<>();
        for (Friendship friendship : friendships.values()) {
            if (friendship.getUser().getId() == userId) {
                friends.add(friendship.getUser());
            }
        }
        if (friends.isEmpty()) {
            throw new FriendshipNotFoundException("There are no friends yet");
        }

        log.info("There are {} friends found", friends.size());
        return friends;
    }

    public int generateUserId() {
        return nextUserId++;
    }

    public int generateFriendshipId() {
        return nextFriendshipId++;
    }
}
