package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UnknownItemUpdateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Objects;

@Service
public class FilmService {

    @Autowired
    FilmStorage filmStorage;


    public List<Film> getAllEntries() {
        return filmStorage.getAllEntries();
    }

    public Film getFilm(Long filmId) {
        Film loadFilm = filmStorage.get(filmId);
        if (Objects.isNull(loadFilm)) {
            throw new NotFoundException(String.format("Фильм с id=%x " +
                    "не найден", filmId));
        }
        return filmStorage.get(filmId);
    }

    public Film saveFilm(Film film) {
        Film filmLoad = filmStorage.get(film.getId());
        filmStorage.save(film);
        return film;
    }

    public Film updateFilm(Film film) {
        Film filmUpdated = filmStorage.update(film);
        if (Objects.nonNull(filmUpdated)) {
            return filmUpdated;
        } else {
            throw new UnknownItemUpdateException(String.format("Фильм с id=%x" +
                    " не найден", film.getId()));
        }
    }
}
