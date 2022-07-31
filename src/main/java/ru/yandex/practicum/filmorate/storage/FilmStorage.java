package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllEntries();

    Film get(Long id);

    void save(Film film);

    Film update(Film film);

    void clear();
}
