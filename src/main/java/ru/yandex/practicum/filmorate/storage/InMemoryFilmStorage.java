package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    private Long id = 1L;

    public List<Film> getAllEntries() {
        return new ArrayList<>(films.values());
    }

    public Optional<Film> get(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    public void save(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
    }

    public Film update(Film film) {
        if (Objects.isNull(films.get(film.getId()))) {
            return null;
        }
        films.replace(film.getId(), film);
        return film;
    }

    public void clear() {
        this.id = 1L;
        films.clear();
    }
}