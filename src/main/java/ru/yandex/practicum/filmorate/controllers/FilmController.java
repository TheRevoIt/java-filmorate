package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    FilmService filmService;

    @GetMapping()
    public List<Film> getAllFilms() {
        log.info("Список фильмов получен");
        return filmService.getAllEntries();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable Long filmId) {
        log.info("Фильм получен");
        return filmService.getFilm(filmId);
    }

    @PostMapping
    public Film saveFilm(@Valid @RequestBody Film film) {
        Film saved = filmService.saveFilm(film);
        log.info("Фильм добавлен");
        return saved;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Фильм обновлен");
        return filmService.updateFilm(film);
    }
}