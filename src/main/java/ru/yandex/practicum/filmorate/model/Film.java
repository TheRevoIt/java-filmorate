package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validators.DateIsAfter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film implements Model {
    @NotEmpty
    private final String name;
    @Size(max = 200)
    private final String description;
    @DateIsAfter
    private final LocalDate releaseDate;
    @Positive
    private final Integer duration;
    private Integer id;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}