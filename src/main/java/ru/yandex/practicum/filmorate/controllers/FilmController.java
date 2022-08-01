package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")

@Validated
public class FilmController {
    @Autowired
    private FilmService filmService;

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

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id,
                         @PathVariable Long userId) {
        filmService.likeFilm(id, userId);
        log.info(String.format("Пользователь id=%x" +
                "лайкнул фильм id=%x", userId, id));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id,
                           @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
        log.info(String.format("Пользователь id=%x" +
                " убрал лайк с фильма id=%x", userId, id));
    }

    @GetMapping("/popular")
    public List<Film> getFilmsSortedByLikes(@Positive @RequestParam(name = "count", required = false,
            defaultValue = "10") Integer count) {
        List<Film> filmListSorted = filmService.getFilmsSortedByLikes(count);
        log.info(String.format("Список из %d фильмов, отсортированный по количеству" +
                " лайков получен", count));
        return filmListSorted;
    }

    @GetMapping("/clear")
    public void clearAfterTest() {
        filmService.clearMapForTests();
    }
}