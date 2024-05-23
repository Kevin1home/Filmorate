package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.excepsions.FriendshipAlreadyExistException;
import ru.yandex.practicum.filmorate.excepsions.FriendshipNotFoundException;
import ru.yandex.practicum.filmorate.excepsions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.excepsions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM app_user ORDER BY id";
        List<User> allUsers = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));

        /*if (allUsers.isEmpty()) {
            throw new UserNotFoundException("There are no users yet");
        }*/
        log.info("There are {} users found", allUsers.size());
        return allUsers;
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM app_user WHERE id=?";
        List<User> userList = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);

        if (userList.isEmpty()) {
            throw new UserNotFoundException("No user under such ID found");
        }
        return userList.getLast();
    }

    @Override
    public User addUser(User user) {
        for (User existingUser : getAllUsers()) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                throw new UserAlreadyExistException("User already exists");
            }
        }

        String sql = "INSERT INTO app_user (login, name, email, birthday) " +
                     "VALUES (?, ?, ?, ?)";
        String login = user.getLogin();
        String name = user.getName();
        String email = user.getEmail();
        Date birthday = Date.valueOf(user.getBirthday());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int qnt = jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, login);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setDate(4, birthday);
            return stmt;
        }, keyHolder);
        log.info("Added {} new records to table app_user", qnt);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        getUserById(user.getId()); // for checking whether exists

        String sql = "UPDATE app_user " +
                     "SET login=?, name=?, email=?, birthday=? " +
                     "WHERE id=?";
        String login = user.getLogin();
        String name = user.getName();
        String email = user.getEmail();
        Date birthday = Date.valueOf(user.getBirthday());
        int id = user.getId();

        int qnt = jdbcTemplate.update(sql, login, name, email, birthday, id);
        log.info("Updated {} records by table app_user", qnt);

        return getUserById(id);
    }

    @Override
    public Friendship addFriend(Friendship friendship) {
        int userId = friendship.getUser().getId();
        int friendId = friendship.getFriend().getId();

        if (getFriendship(userId, friendId).isPresent()) {
            throw new FriendshipAlreadyExistException(
                    String.format("User with ID %s already has a user with ID %s as friend", userId, friendId));
        }

        String sql = "INSERT INTO friendship (user_id, friend_id, status) " +
                     "VALUES (?, ?, ?)";
        boolean status = friendship.getStatus();

        int qnt = jdbcTemplate.update(sql, userId, friendId, status);
        log.info("Added {} new records to table friendship", qnt);

        Optional<Friendship> newFriendship = getFriendship(userId, friendId);
        if (newFriendship.isEmpty()) {
            log.error("Friendship between {} and {} not found after adding to DB!", userId, friendId);
            throw new FriendshipNotFoundException(
                    String.format("Error by adding new friendship between %s and %s", userId, friendId));
        }
        return newFriendship.get();
    }

    @Override
    public void updateFriendship(Friendship friendship) {
        String sql = "UPDATE friendship " +
                     "SET status=? " +
                     "WHERE id=?";
        int id = friendship.getId();
        boolean status = friendship.getStatus();

        int qnt = jdbcTemplate.update(sql, status, id);
        log.info("Updated {} records by table friendship", qnt);
    }

    @Override
    public Optional<Friendship> getFriendship(int userId, int friendId) {
        String sql = "SELECT * FROM friendship WHERE user_id=? AND friend_id=?";
        List<Friendship> friendshipList = jdbcTemplate.query(sql, (rs, rowNum) -> makeFriendship(rs), userId, friendId);

        if (friendshipList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(friendshipList.getLast());
    }

    @Override
    public void deleteFriend(Friendship friendship) {
        String sql = "DELETE FROM friendship WHERE id=?";
        int id = friendship.getId();

        int qnt = jdbcTemplate.update(sql, id);
        log.info("Deleted {} records from table friendship", qnt);
    }

    @Override
    public List<User> findFriends(int userId) {
        getUserById(userId); // for checking whether exists

        String sql = "SELECT * FROM friendship WHERE user_id=?";
        List<Friendship> allFriendships = jdbcTemplate.query(sql, (rs, rowNum) -> makeFriendship(rs), userId);

        if (allFriendships.isEmpty()) {
            throw new FriendshipNotFoundException("There are no friendship yet");
        }
        List<User> allFriends = allFriendships.stream()
                .map(Friendship::getFriend)
                .collect(Collectors.toList());

        log.info("There are {} friends by user {} found", allFriends.size(), userId);
        return allFriends;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String login = rs.getString("login");
        String name = rs.getString("name");
        String email = rs.getString("email");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        User user = new User(login, email, birthday);
        user.setId(id);
        user.setName(name);

        return user;
    }

    private Friendship makeFriendship(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        int friendId = rs.getInt("friend_id");
        boolean status = rs.getBoolean("status");

        return new Friendship(id, getUserById(userId), getUserById(friendId), status);
    }

}
