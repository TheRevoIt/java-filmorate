package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;

@SpringBootApplication
@ComponentScan(basePackageClasses = {FilmController.class, UserController.class})
public class FilmorateApplication {
    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }
}