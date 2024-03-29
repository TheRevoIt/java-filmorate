package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Friendship {
    @NonNull
    private final Long userId;
    @NonNull
    private final Long friendId;
}
