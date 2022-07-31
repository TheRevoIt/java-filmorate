package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> filmsMap = new HashMap<>();
    private Long id = 1L;

    public List<Film> getAllEntries() {
        return new ArrayList<>(filmsMap.values());
    }

    public Film get(Long id) {
        return filmsMap.get(id);
    }

    public void save(Film film) {
        film.setId(id++);
        filmsMap.put(film.getId(), film);
    }

    public Film update(Film film) {
        if (Objects.isNull(filmsMap.get(film.getId()))) {
            return null;
        }
        filmsMap.replace(film.getId(), film);
        return film;
    }

    public void clear() {
        this.id = 1L;
        filmsMap.clear();
    }
}