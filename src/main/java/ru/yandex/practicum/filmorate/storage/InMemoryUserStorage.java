package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> usersMap = new HashMap<>();
    private Long id = 1L;

    public List<User> getAllEntries() {
        return new ArrayList<>(usersMap.values());
    }

    public User get(Long id) {
        return usersMap.get(id);
    }

    public void save(User user) {
        user.setId(id++);
        usersMap.put(user.getId(), user);
    }

    public User update(User user) {
        if (Objects.isNull(usersMap.get(user.getId()))) {
            return null;
        }
        usersMap.replace(user.getId(), user);
        return user;
    }

    public void clear() {
        this.id = 1L;
        usersMap.clear();
    }
}