package ru.yandex.practicum.filmorate.storage.implementation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
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
    public List<Long> getFriendList(Long userId) {
        String sqlQuery = "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, userId);
    }

    @Override
    public List<Long> getCommonFriends(Friendship friendship) {
        String sqlQuery = "SELECT FirstUserFriends.id FROM (SELECT FRIEND_ID id FROM friends WHERE USER_ID = ? UNION " +
                "SELECT USER_ID UserId FROM friends WHERE FRIEND_ID = ?) AS FirstUserFriends " +
                "JOIN (SELECT FRIEND_ID id FROM friends WHERE USER_ID = ? UNION" +
                " SELECT USER_ID UserId FROM friends WHERE FRIEND_ID = ?) AS" +
                " SecondUserFriends ON FirstUserFriends.id = SecondUserFriends.id";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, friendship.getUserId(), friendship.getUserId(),
                friendship.getFriendId(), friendship.getFriendId());
    }
}