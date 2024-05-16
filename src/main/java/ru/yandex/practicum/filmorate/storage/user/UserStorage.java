package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.excepsions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;

public interface UserStorage {

    HashMap<Integer, User> getAllUsers();

    User addUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

    int generateId();

}
