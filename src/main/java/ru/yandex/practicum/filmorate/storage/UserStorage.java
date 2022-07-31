package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllEntries();

    User get(Long id);

    void save(User user);

    User update(User user);

    void clear();
}
