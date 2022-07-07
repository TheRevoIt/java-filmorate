package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validators.DateIsAfter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film implements Model {
    @NotEmpty
    private final String name;
    @NotNull
    @Size(max = 200)
    private final String description;
    @NotNull
    @DateIsAfter
    private final LocalDate releaseDate;
    @NotNull
    @Positive
    private final Integer duration;
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}