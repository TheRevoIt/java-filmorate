package ru.yandex.practicum.filmorate.exception;

public class UnknownItemUpdateException extends RuntimeException{
    public UnknownItemUpdateException(String message) {
        super(message);
    }
}
