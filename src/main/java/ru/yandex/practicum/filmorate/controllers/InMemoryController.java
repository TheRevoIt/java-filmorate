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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
abstract class InMemoryController<T extends Model> {
    private final Map<Long, T> list = new HashMap<>();
    private Long id = 1L;

    @GetMapping()
    public List<T> getItems() {
        log.info("Список получен");
        return new ArrayList<>(list.values());
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