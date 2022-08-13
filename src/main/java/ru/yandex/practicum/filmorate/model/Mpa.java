package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Mpa {
    private final Long id;
    @NotNull
    private final String name;
}
