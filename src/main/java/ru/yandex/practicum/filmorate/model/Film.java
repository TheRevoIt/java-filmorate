package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validators.DateIsAfter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Slf4j
@Data
@JsonPropertyOrder({"name", "description", "releaseDate", "duration", "id", "mpa"})
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
    @NotNull
    private Mpa mpa;
    private Set<Genre> genres;

    @JsonCreator
    public Film(@JsonProperty("name") String name,
                @JsonProperty("description") String description,
                @JsonProperty("releaseDate") LocalDate releaseDate,
                @JsonProperty("duration") Integer duration,
                @JsonProperty("mpa") Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public Film(String name,
                String description,
                LocalDate releaseDate,
                Integer duration,
                Mpa mpa,
                Long id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}