package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Friendship {
    @NotNull
    private final Long userId;
    @NotNull
    private final Long friendId;
}
