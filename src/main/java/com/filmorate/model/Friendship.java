package com.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a friendship relationship between two users.
 * <p>
 * Each instance contains:
 * <ul>
 *   <li>user – the user who sent the friend request</li>
 *   <li>friend – the user who confirms the friendship</li>
 *   <li>status – true if confirmed, false otherwise</li>
 * </ul>
 */
@Getter
@Setter
@ToString
public class Friendship {
    private Integer id;
    private final User user; // User who sends the friend request
    private final User friend; // User who confirms the friendship
    private Boolean status; // Friendship status: true = confirmed, false = pending

    public Friendship(User user, User friend, boolean status) {
        this.user = user;
        this.friend = friend;
        this.status = status;
    }

    public Friendship(int id, User user, User friend, boolean status) {
        this.id = id;
        this.user = user;
        this.friend = friend;
        this.status = status;
    }
}
