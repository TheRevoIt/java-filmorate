package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {
    Set<Genre> getFilmGenres(Long id);

    Optional<Genre> get(Long id);

    List<Genre> getAllEntries();
}
