package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Mpa get(Long id) {
        return mpaStorage.get(id).orElseThrow(() -> new NotFoundException(String.format("Рейтинг с id=%x " +
                "не найден", id)));
    }

    public List<Mpa> getAllEntries() {
        return mpaStorage.getAllEntries();
    }
}
