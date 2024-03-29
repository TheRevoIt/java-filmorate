package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getAllEntries();

    Optional<User> get(Long id);

    User save(User user);

    User update(User user);

    void delete(Long id);

    void clear();
}
