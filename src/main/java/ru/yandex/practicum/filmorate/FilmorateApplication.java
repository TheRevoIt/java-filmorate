package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.controllers.InMemoryController;

@SpringBootApplication
@ComponentScan(basePackageClasses = {InMemoryController.class})
public class FilmorateApplication {
    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }
}