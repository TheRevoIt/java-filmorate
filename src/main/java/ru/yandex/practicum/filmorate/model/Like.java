package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Like {
    @NonNull
    private final Long userId;
    @NonNull
    private final Long filmId;
}
