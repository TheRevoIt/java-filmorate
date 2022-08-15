package ru.yandex.practicum.filmorate.storage.implementation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.util.List;

@Repository
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriends(Friendship friendship) {
        String sqlQuery = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, friendship.getUserId(), friendship.getFriendId());
    }

    @Override
    public void deleteFriend(Friendship friendship) {
        String sqlQuery = "DELETE FROM FRIENDS WHERE (USER_ID = ? AND FRIEND_ID = ? )";
        jdbcTemplate.update(sqlQuery, friendship.getUserId(), friendship.getFriendId());
    }

    @Override
    public List<User> getFriendList(Long userId) {
        String sqlQuery = "SELECT * FROM USERS where Users.USER_ID " +
                "in (SELECT FRIEND_ID from FRIENDS where FRIENDS.USER_ID = ?)";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::mapRowToUser, userId);
    }

    @Override
    public List<User> getCommonFriends(Friendship friendship) {
        String sqlQuery = "select * from USERS u, FRIENDS f, FRIENDS o where u.USER_ID = f.FRIEND_ID AND" +
                " u.USER_ID = o.FRIEND_ID AND f.USER_ID = ? AND o.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::mapRowToUser,
                friendship.getFriendId(), friendship.getUserId());
    }
}