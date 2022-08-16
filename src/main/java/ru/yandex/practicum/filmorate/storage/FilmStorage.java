package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getAllEntries();

    Optional<Film> get(Long id);

    void save(Film film);

    Film update(Film film);

    void delete(Long id);

    void clear();
}
