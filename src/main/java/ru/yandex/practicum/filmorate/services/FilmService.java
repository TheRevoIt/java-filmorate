package ru.yandex.practicum.filmorate.services;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikesStorage likesStorage;
    private final GenreStorage genreStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, GenreStorage genreStorage, LikesStorage likesStorage,
                       UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.likesStorage = likesStorage;
        this.genreStorage = genreStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAllEntries() {
        List<Film> result = new ArrayList<>();
        for (Film film : filmStorage.getAllEntries()) {
            Set<Genre> genres = genreStorage.getFilmGenres(film.getId());
            film.setGenres(genres);
            result.add(film);
        }
        return result;
    }

    public Film getFilm(Long filmId) {
        Set<Genre> genres = genreStorage.getFilmGenres(filmId);
        Film filmLoaded = filmStorage.get(filmId).orElseThrow(() -> new NotFoundException(String.format("Фильм с id=%x " +
                "не найден", filmId)));
        filmLoaded.setGenres(genres);
        return filmLoaded;
    }

    public Film saveFilm(Film film) {
        filmStorage.save(film);
        return getFilm(film.getId());
    }

    public Film updateFilm(Film film) {
        Film filmUpdated = filmStorage.update(film);
        Set<Genre> genres = genreStorage.getFilmGenres(film.getId());
        if (Objects.nonNull(filmUpdated)) {
            filmUpdated.setGenres(genres);
            return filmUpdated;
        } else {
            throw new NotFoundException(String.format("Фильм с id=%x" +
                    " не найден", film.getId()));
        }
    }

    public void deleteFilm(Long id) {
        getFilm(id);
        filmStorage.delete(id);
    }

    public void likeFilm(Long id, Long userId) {
        checkIfUserExists(userId);
        getFilm(id);
        likesStorage.likeFilm(new Like(userId, id));
    }

    public void deleteLike(Long id, Long userId) {
        checkIfUserExists(userId);
        getFilm(id);
        likesStorage.deleteLike(new Like(userId, id));
    }

    public List<Film> getFilmsSortedByLikes(Integer size) {
        return likesStorage.getFilmsSortedByLikes(size);
    }

    public void clearMapForTests() {
        filmStorage.clear();
    }

    private void checkIfUserExists(Long userId) {
        userStorage.get(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь" +
                " с id=%x не найден", userId)));
    }
}