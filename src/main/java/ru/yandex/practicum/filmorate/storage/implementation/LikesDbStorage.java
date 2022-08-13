package ru.yandex.practicum.filmorate.storage.implementation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.List;

@Repository
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void likeFilm(Like like) {
        String sqlQuery = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, like.getFilmId(), like.getUserId());
    }

    @Override
    public void deleteLike(Like like) {
        String sqlQuery = "DELETE FROM LIKES WHERE USER_ID = ? AND FILM_ID = ? ";
        jdbcTemplate.update(sqlQuery, like.getUserId(), like.getFilmId());
    }

    @Override
    public List<Long> getFilmsSortedByLikes(Integer size) {
        String sqlQuery = "SELECT F.FILM_ID from FILMS AS F left join (SELECT * FROM LIKES group by USER_ID, FILM_ID) AS M" +
                " ON F.FILM_ID=M.FILM_ID GROUP BY F.FILM_ID ORDER BY COUNT(USER_ID) DESC LIMIT ?";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, size);
    }
}