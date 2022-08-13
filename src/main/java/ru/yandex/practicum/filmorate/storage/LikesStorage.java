package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikesStorage {
    void likeFilm(Like like);

    void deleteLike(Like like);

    List<Long> getFilmsSortedByLikes(Integer size);

}
