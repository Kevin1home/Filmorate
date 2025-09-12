package com.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.filmorate.excepsion.friendship.FriendshipAlreadyExistException;
import com.filmorate.excepsion.friendship.FriendshipNotFoundException;
import com.filmorate.excepsion.user.UserAlreadyExistException;
import com.filmorate.excepsion.user.UserNotFoundException;
import com.filmorate.model.Friendship;
import com.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Database-backed implementation of {@link UserStorage}.
 * <p>
 * Stores users and friendships in a relational database using {@link JdbcTemplate}.
 * Provides CRUD operations and friendship management.
 */
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

        log.info("Retrieved {} users", allUsers.size());
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
        boolean exists = getAllUsers().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()));
        if (exists) {
            throw new UserAlreadyExistException("User with email " + user.getEmail() + " already exists");
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
        getUserById(user.getId()); // ensure user exists

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
                    String.format("User %s already has user %s as friend", userId, friendId));
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
        getUserById(userId); // ensure user exists

        String sql = "SELECT * FROM friendship WHERE user_id=?";
        List<Friendship> allFriendships = jdbcTemplate.query(sql, (rs, rowNum) -> makeFriendship(rs), userId);

        if (allFriendships.isEmpty()) {
            throw new FriendshipNotFoundException("User " + userId + " has no friends");
        }

        List<User> allFriends = allFriendships.stream()
                .map(Friendship::getFriend)
                .collect(Collectors.toList());

        log.info("Retrieved {} friends for user {}", allFriends.size(), userId);
        return allFriends;
    }

    //-----------------------
    //Private helpers methods
    //-----------------------

    /**
     * Maps a SQL result set to a {@link User} object.
     *
     * @param rs the ResultSet
     * @return a User object
     * @throws SQLException if mapping fails
     */
    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String login = rs.getString("login");
        String name = rs.getString("name");
        String email = rs.getString("email");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(id, login, name, email, birthday);
    }

    /**
     * Maps a SQL result set to a {@link Friendship} object.
     *
     * @param rs the ResultSet
     * @return a Friendship object
     * @throws SQLException if mapping fails
     */
    private Friendship makeFriendship(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        int friendId = rs.getInt("friend_id");
        boolean status = rs.getBoolean("status");

        return new Friendship(id, getUserById(userId), getUserById(friendId), status);
    }
}
