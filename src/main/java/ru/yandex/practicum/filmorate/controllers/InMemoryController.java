package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Model;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class InMemoryController<T extends Model> {
    private final Map<Integer, T> list = new HashMap<>();
    private int id = 1;

    @GetMapping()
    public Collection<T> getItems() {
        log.info("Список получен");
        return list.values();
    }

    @PostMapping()
    public T addItem(@Valid @RequestBody T item) {
        item.setId(id++);
        if (!list.containsKey(item.getId())) {
            list.put(item.getId(), item);
        }
        log.info("Объект добавлен");
        return item;
    }

    @PutMapping()
    public T updateItem(@Valid @RequestBody T item) {
        if (list.containsKey(item.getId())) {
            list.replace(item.getId(), item);
            log.info("Объект обновлен");
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return item;
    }
}