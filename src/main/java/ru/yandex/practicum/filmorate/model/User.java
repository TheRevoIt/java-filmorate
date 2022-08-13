package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validators.NoWhiteSpaces;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@Data
@JsonPropertyOrder({"id", "email", "login", "name", "birthday"})
public class User implements Model {
    @JsonProperty
    private Long id;
    @NotNull
    @Email
    private String email;
    @NotEmpty
    @NoWhiteSpaces
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    @JsonCreator
    public User(@JsonProperty("email") String email,
                @JsonProperty("login") String login,
                @JsonProperty("name") String name,
                @JsonProperty("birthday") LocalDate birthday) {
        this.email = email;
        this.login = login;
        if (Objects.isNull(name) || name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }

    public User(String email,
                String login,
                String name,
                LocalDate birthday,
                Long id) {
        this.email = email;
        this.login = login;
        if (Objects.isNull(name) || name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
        this.id = id;
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