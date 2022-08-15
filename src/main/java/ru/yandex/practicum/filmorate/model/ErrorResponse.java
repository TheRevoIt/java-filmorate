package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
class ErrorResponse {
    String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}