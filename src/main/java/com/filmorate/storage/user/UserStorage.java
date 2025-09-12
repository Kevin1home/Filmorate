package com.filmorate.storage.user;

import com.filmorate.model.Friendship;
import com.filmorate.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Interface defining storage operations for {@link User} and {@link Friendship}.<br>
 * Implementations may store data in memory or in a database.
 */
public interface UserStorage {

    List<User> getAllUsers();

    User getUserById(int id);

    User addUser(User user);

    User updateUser(User user);

    Friendship addFriend(Friendship friendship);

    void updateFriendship(Friendship friendship);

    Optional<Friendship> getFriendship(int userId, int friendId);

    void deleteFriend(Friendship friendship);

    List<User> findFriends(int userId);
}
