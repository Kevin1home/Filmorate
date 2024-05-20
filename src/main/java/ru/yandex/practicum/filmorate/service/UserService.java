package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.excepsions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public User addFriend(Integer userId, Integer friendId) {
        if (userId == null || userId <= 0) {
            throw new NotFoundException("userId");
        }
        if (friendId == null || friendId <= 0) {
            throw new NotFoundException("friendId");
        }
        if (!userStorage.getAllUsers().containsKey(userId)) {
            throw new NotFoundException("userId");
        }
        if (!userStorage.getAllUsers().containsKey(friendId)) {
            throw new NotFoundException("friendId");
        }
        userStorage.getAllUsers().get(userId).getFiends().add(friendId);
        log.info("Пользователь с ID {} добавлен в друзья к пользователю с ID {}", userId, friendId);
        userStorage.getAllUsers().get(friendId).getFiends().add(userId);
        log.info("Пользователь с ID {} добавлен в друзья к пользователю с ID {}", friendId, userId);

        return userStorage.getAllUsers().get(userId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        if (userId == null || userId <= 0) {
            throw new NotFoundException("userId");
        }
        if (friendId == null || friendId <= 0) {
            throw new NotFoundException("friendId");
        }
        if (!userStorage.getAllUsers().containsKey(userId)) {
            throw new NotFoundException("userId");
        }
        if (!userStorage.getAllUsers().containsKey(friendId)) {
            throw new NotFoundException("friendId");
        }
        if (!userStorage.getAllUsers().get(userId).getFiends().contains(friendId)) {
            log.info("Пользователя с ID {} не было в друзьях пользователя с ID {}", userId, friendId);
            return userStorage.getAllUsers().get(friendId);
        }
        userStorage.getAllUsers().get(userId).getFiends().remove(friendId);
        log.info("Пользователь с ID {} удалён из друзей пользователя с ID {}", userId, friendId);
        userStorage.getAllUsers().get(friendId).getFiends().remove(userId);
        log.info("Пользователь с ID {} удалён из друзей пользователя с ID {}", friendId, userId);
        return userStorage.getAllUsers().get(friendId);
    }

    public List<User> findCommonFriends(Integer userId, Integer otherId) {
        if (userId == null || userId <= 0) {
            throw new NotFoundException("userId");
        }
        if (otherId == null || otherId <= 0) {
            throw new NotFoundException("otherId");
        }
        if (!userStorage.getAllUsers().containsKey(userId)) {
            throw new NotFoundException("userId");
        }
        if (!userStorage.getAllUsers().containsKey(otherId)) {
            throw new NotFoundException("otherId");
        }
        List<User> commonFriends = new ArrayList<>();
        for (Integer friendId : userStorage.getAllUsers().get(userId).getFiends()) {
            if (userStorage.getAllUsers().get(otherId).getFiends().contains(friendId)) {
                commonFriends.add(userStorage.getAllUsers().get(friendId));
            }
        }
        log.info("Итого общих друзей: {}", commonFriends.size());
        return commonFriends;
    }

    public List<Integer> findFriends(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new NotFoundException("userId");
        }
        if (!userStorage.getAllUsers().containsKey(userId)) {
            throw new NotFoundException("userId");
        }
        log.info("Всего друзей: {}", userStorage.getAllUsers().get(userId).getFiends().size());
        return userStorage.getAllUsers().get(userId).getFiends().stream().toList();
    }

}
