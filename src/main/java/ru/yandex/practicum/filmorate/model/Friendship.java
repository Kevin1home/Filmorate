package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Friendship {
    private Integer id;
    private User user; // who adding
    private User friend; // who confirming
    private Boolean status; // true = is confirmed, false = is not confirmed

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
