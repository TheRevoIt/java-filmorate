package ru.yandex.practicum.filmorate.storage.implementation;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

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
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    static Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film filmMapped = new Film(resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getInt("duration"),
                new Mpa(resultSet.getLong("MPA.MPA_ID"),
                        resultSet.getString("MPA.NAME")));
        filmMapped.setId(resultSet.getLong("film_id"));
        return filmMapped;
    }

    @Override
    public List<Film> getAllEntries() {
        String sqlQuery = "SELECT * FROM FILMS, MPA WHERE FILMS.MPA_ID = MPA.MPA_ID";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::mapRowToFilm);
    }

    @Override
    public Optional<Film> get(Long id) {
        String sqlQuery = "SELECT * FROM FILMS f, MPA m WHERE f.MPA_ID = m.MPA_ID AND FILM_ID = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, FilmDbStorage::mapRowToFilm, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Film film) {
        String sqlQuery = "INSERT INTO FILMS (TITLE, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (Objects.isNull(releaseDate)) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        addFilmGenres(film);
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILMS SET " +
                "TITLE = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? " +
                "WHERE FILM_ID = ?";
        int res = jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());
        deleteFilmGenres(film);
        addFilmGenres(film);
        if (res > 0) {
            return film;
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID=?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void clear() {
        String sqlQuery = "DELETE " +
                "FROM FILMS";
        String sqlQueryId = "ALTER TABLE FILMS " +
                "ALTER COLUMN FILM_ID RESTART WITH 1 ";
        jdbcTemplate.update(sqlQuery);
        jdbcTemplate.update(sqlQueryId);
    }

    private void addFilmGenres(Film film) {
        if (Objects.nonNull(film.getGenres())) {
            String sqlQueryGenre = "MERGE INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
            film.getGenres().forEach(p -> jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(sqlQueryGenre);
                statement.setLong(1, film.getId());
                statement.setLong(2, p.getId());
                return statement;
            }));
        }
    }

    private void deleteFilmGenres(Film film) {
        String sqlQueryDelete = "DELETE FROM FILM_GENRES";
        jdbcTemplate.update(sqlQueryDelete);
    }
}