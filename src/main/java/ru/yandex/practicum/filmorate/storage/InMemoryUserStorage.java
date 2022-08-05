package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> users = new HashMap<>();
    private Long id = 1L;

    public List<User> getAllEntries() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> get(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public void save(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
    }

    public User update(User user) {
        if (Objects.isNull(users.get(user.getId()))) {
            return null;
        }
        users.replace(user.getId(), user);
        return user;
    }

    public void clear() {
        this.id = 1L;
        users.clear();
    }
}