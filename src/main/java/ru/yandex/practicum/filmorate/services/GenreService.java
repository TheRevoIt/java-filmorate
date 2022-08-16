package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre get(Long id) {
        return genreStorage.get(id).orElseThrow(() -> new NotFoundException(String.format("Жанр с id=%x " +
                "не найден", id)));
    }

    public List<Genre> getAllEntries() {
        return genreStorage.getAllEntries();
    }
}