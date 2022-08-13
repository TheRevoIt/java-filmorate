package ru.yandex.practicum.filmorate.storage.implementation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmDbStorageTest {
    private final FilmStorage filmStorage;
    private final MpaDbStorage mpaStorage;

    @Autowired
    FilmDbStorageTest(FilmDbStorage filmStorage, MpaDbStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage;
    }

    @AfterEach
    void clearStorageAfterEachTest() {
        filmStorage.clear();
    }

    @Test
    void getAllEntriesTest() {
        Film film = new Film("Title", "Description", LocalDate.of(1955, 12, 5),
                100, mpaStorage.get(2L).get());
        filmStorage.save(film);

        Optional<Film> filmOptional = filmStorage.get(1L);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film1 ->
                        assertThat(film1).hasFieldOrPropertyWithValue("name", "Title"));
    }

    @Test
    void getFilmByIdTest() {
        Film film = new Film("Title", "Description", LocalDate.of(1955, 12, 5),
                100, mpaStorage.get(3L).get());
        Film film1 = new Film("Title1", "Description1", LocalDate.of(1955, 12, 12),
                100, mpaStorage.get(2L).get());
        filmStorage.save(film);
        filmStorage.save(film1);
        List<Film> referenceList = new ArrayList<>(Arrays.asList(film, film1));
        List<Film> loadedFilmList = filmStorage.getAllEntries();
        assertEquals(referenceList, loadedFilmList, "Возвращается некорректный список фильмов");
    }

    @Test
    void saveFilmTest() {
        Film film = new Film("Title", "Description", LocalDate.of(1955, 12, 5),
                100, mpaStorage.get(2L).get());
        filmStorage.save(film);

        Optional<Film> filmOptional = filmStorage.get(1L);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film1 ->
                        assertThat(film1).hasFieldOrPropertyWithValue("name", "Title")
                                .hasFieldOrPropertyWithValue("duration", 100));
    }

    @Test
    void updateFilmTest() {
        Film film = new Film("Title", "Description", LocalDate.of(1955, 12, 5),
                100, mpaStorage.get(2L).get());
        filmStorage.save(film);
        filmStorage.update(new Film("Title", "Update Description", LocalDate.of(1955, 12, 5),
                150, mpaStorage.get(2L).get(), 1L));
        Optional<Film> filmOptional = filmStorage.get(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film1 ->
                        assertThat(film1).hasFieldOrPropertyWithValue("description", "Update Description")
                                .hasFieldOrPropertyWithValue("duration", 150));
    }

    @Test
    void deleteFilmTest() {
        Film film = new Film("Title", "Description", LocalDate.of(1955, 12, 5),
                100, mpaStorage.get(2L).get());
        filmStorage.save(film);
        filmStorage.save(new Film("Title", "Update Description", LocalDate.of(1955, 12, 5),
                150, mpaStorage.get(2L).get()));
        filmStorage.delete(2L);
        assertEquals(1, filmStorage.getAllEntries().size(), "Удаление фильма происходит некорректно");
    }
}