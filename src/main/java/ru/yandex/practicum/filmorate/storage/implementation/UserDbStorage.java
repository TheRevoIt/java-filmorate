package ru.yandex.practicum.filmorate.storage.implementation;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllEntries() {
        String sqlQuery = "SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY FROM USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public Optional<User> get(Long id) {
        String sqlQuery = "SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY FROM USERS WHERE USER_ID = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public User save(User user) {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (Objects.isNull(birthday)) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return get(user.getId()).get();
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE USERS SET " +
                "EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? " +
                "WHERE USER_ID = ?";
        int res = jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
        if (res > 0) {
            return get(user.getId()).get();
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        String sqlQuery = "DELETE FROM USERS WHERE USER_ID=?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void clear() {
        String sqlQuery = "DELETE " +
                "FROM USERS";
        String sqlQueryId = "ALTER TABLE USERS " +
                "ALTER COLUMN USER_ID RESTART WITH 1 ";
        jdbcTemplate.update(sqlQuery);
        jdbcTemplate.update(sqlQueryId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User userMapped = new User(resultSet.getString("email"), resultSet.getString("login"), resultSet.getString("user_name"), resultSet.getDate("birthday").toLocalDate());
        userMapped.setId(resultSet.getLong("user_id"));
        return userMapped;
    }
}
