package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

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
