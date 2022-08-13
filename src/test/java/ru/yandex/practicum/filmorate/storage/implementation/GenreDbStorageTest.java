package ru.yandex.practicum.filmorate.storage.implementation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
class GenreDbStorageTest {
    private final GenreStorage genreStorage;
    private final MpaDbStorage mpaStorage;
    private final FilmStorage filmStorage;

    @Autowired
    GenreDbStorageTest(GenreStorage genreStorage,
                       MpaDbStorage mpaStorage,
                       FilmStorage filmStorage) {
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.filmStorage = filmStorage;
    }

    @Test
    void getByIdTest() {
        Genre genre = genreStorage.get(2L).get();
        assertEquals("Драма", genre.getName());
    }

    @Test
    void getAllGenresTest() {
        List<Genre> genresList = genreStorage.getAllEntries();
        assertEquals(genresList.get(0).getName(), "Комедия");
        assertEquals(genresList.get(1).getName(), "Драма");
    }

    @Test
    void getFilmGenres() {
        Film film = new Film("Title", "Description", LocalDate.of(1955, 12, 5),
                100, mpaStorage.get(3L).get());
        Set<Genre> filmGenres = new HashSet<>();
        filmGenres.add(genreStorage.get(1L).get());
        filmGenres.add(genreStorage.get(2L).get());
        film.setGenres(filmGenres);
        filmStorage.save(film);
        assertEquals(2, genreStorage.getFilmGenres(1L).size());
        assertTrue(genreStorage.getFilmGenres(1L).contains(new Genre(2, "Драма")));
        assertTrue(genreStorage.getFilmGenres(1L).contains(new Genre(1, "Комедия")));
        filmStorage.clear();
    }
}