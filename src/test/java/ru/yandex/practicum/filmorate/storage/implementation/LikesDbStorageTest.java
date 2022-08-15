package ru.yandex.practicum.filmorate.storage.implementation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
class LikesDbStorageTest {
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;

    @Autowired
    LikesDbStorageTest(UserStorage userStorage,
                       LikesStorage likesStorage,
                       FilmStorage filmStorage,
                       MpaStorage mpaStorage) {
        this.userStorage = userStorage;
        this.likesStorage = likesStorage;
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage;
    }

    @AfterEach
    void clearStorageAfterEachTest() {
        filmStorage.clear();
        userStorage.clear();
    }

    @Test
    void addLikeTest() {
        Film film = new Film("Title", "Description", LocalDate.of(1955, 12, 5),
                100, mpaStorage.get(2L).get());
        Film film1 = new Film("Title2", "Description2", LocalDate.of(1955, 12, 5),
                100, mpaStorage.get(2L).get());
        filmStorage.save(film);
        filmStorage.save(film1);
        User user = new User("mail@mail.ru", "login", "name",
                LocalDate.of(2001, 12, 2));
        userStorage.save(user);
        Like like = new Like(1L, 1L);
        likesStorage.likeFilm(like);
        assertEquals(1L, likesStorage.getFilmsSortedByLikes(10).get(0).getId(), "Некорреткно обрабатывается лайк пользователем фильма");
    }

    @Test
    void getSortedListByPopularity() {

        Film film = new Film("Title", "Description", LocalDate.of(1955, 12, 5),
                100, mpaStorage.get(2L).get());
        Film film1 = new Film("Title2", "Description2", LocalDate.of(1955, 12, 5),
                100, mpaStorage.get(2L).get());
        filmStorage.save(film);
        filmStorage.save(film1);
        User user = new User("mail@mail.ru", "login", "name",
                LocalDate.of(2001, 12, 2));
        userStorage.save(user);
        User user1 = new User("mail@mail.ru", "login1", "name1",
                LocalDate.of(2001, 12, 2));
        userStorage.save(user);
        Like like = new Like(1L, 1L);
        Like like1 = new Like(2L, 1L);
        Like like2 = new Like(1L, 2L);
        likesStorage.likeFilm(like);
        likesStorage.likeFilm(like1);
        likesStorage.likeFilm(like2);
        assertEquals(1L, likesStorage.getFilmsSortedByLikes(10).get(0).getId());
        assertEquals(2L, likesStorage.getFilmsSortedByLikes(10).get(1).getId());
    }

    @Test
    void removeLikeTest() {
        Film film = new Film("Title", "Description", LocalDate.of(1955, 12, 5),
                100, mpaStorage.get(2L).get());
        Film film1 = new Film("Title2", "Description2", LocalDate.of(1955, 12, 5),
                100, mpaStorage.get(2L).get());
        filmStorage.save(film);
        filmStorage.save(film1);
        User user = new User("mail@mail.ru", "login", "name",
                LocalDate.of(2001, 12, 2));
        userStorage.save(user);
        Like like = new Like(1L, 1L);
        Like like2 = new Like(1L, 2L);
        likesStorage.likeFilm(like);
        likesStorage.likeFilm(like2);
        assertEquals(1, likesStorage.getFilmsSortedByLikes(10).get(0).getId());
        likesStorage.deleteLike(like);
        assertEquals(2, likesStorage.getFilmsSortedByLikes(10).get(0).getId());
    }
}