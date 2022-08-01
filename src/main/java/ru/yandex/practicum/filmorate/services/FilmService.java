package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Autowired
    private FilmStorage filmStorage;

    @Autowired
    private UserStorage userStorage;

    public List<Film> getAllEntries() {
        return filmStorage.getAllEntries();
    }

    public Film getFilm(Long filmId) {
        return filmStorage.get(filmId).orElseThrow(() -> new NotFoundException(String.format("Фильм с id=%x " +
                "не найден", filmId)));
    }

    public Film saveFilm(Film film) {
        filmStorage.save(film);
        return film;
    }

    public Film updateFilm(Film film) {
        Film filmUpdated = filmStorage.update(film);
        if (Objects.nonNull(filmUpdated)) {
            return film;
        } else {
            throw new NotFoundException(String.format("Фильм с id=%x" +
                    " не найден", film.getId()));
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void likeFilm(Long id, Long userId) {
        Film filmLoaded = filmStorage.get(id).orElseThrow(() -> new NotFoundException(String.format("Фильм с id=%x " +
                "не найден", id)));
        User userLoaded = userStorage.get(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь" +
                " с id=%x не найден", userId)));
        Set<Long> filmLikesSet = filmLoaded.getLikesIds();
        filmLikesSet.add(userId);
        filmStorage.get(id).get().setLikesIds(filmLikesSet);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void deleteLike(Long id, Long userId) {
        Film filmLoaded = filmStorage.get(id).orElseThrow(() -> new NotFoundException(String.format("Фильм с id=%x " +
                "не найден", id)));
        User userLoaded = userStorage.get(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь" +
                " с id=%x не найден", userId)));
        Set<Long> filmLikesSet = filmLoaded.getLikesIds();
        filmLikesSet.remove(userId);
        filmStorage.get(id).get().setLikesIds(filmLikesSet);
    }

    public List<Film> getFilmsSortedByLikes(Integer size) {
        List<Film> filmsList = new ArrayList<>(filmStorage.getAllEntries());
        return filmsList.stream().sorted((p0, p1) ->
                        Integer.compare(p1.getLikesIds().size()
                                , p0.getLikesIds().size()))
                .limit(size).collect(Collectors.toList());
    }

    public void clearMapForTests() {
        filmStorage.clear();
    }
}