package ru.yandex.practicum.filmorate.storage.implementation;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Genre> getFilmGenres(Long id) {
        String sqlQuery = "SELECT * FROM GENRE WHERE GENRE.ID IN " +
                "(SELECT FILM_GENRES.GENRE_ID FROM FILM_GENRES WHERE FILM_ID=?)";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id));
    }

    @Override
    public Optional<Genre> get(Long id) {
        String sqlQuery = "SELECT * FROM GENRE WHERE ID=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAllEntries() {
        String sqlQuery = "SELECT * FROM GENRE ORDER BY GENRE.ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getLong("id"), resultSet.getString("name"));
    }
}