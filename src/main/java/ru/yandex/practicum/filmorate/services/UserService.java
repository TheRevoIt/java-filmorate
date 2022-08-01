package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserStorage userStorage;

    public List<User> getAllEntries() {
        return userStorage.getAllEntries();
    }

    public User getUser(Long userId) {
        User userLoaded = userStorage.get(userId);
        if (Objects.isNull(userLoaded)) {
            throw new NotFoundException(String.format("Пользователь с id=%x " +
                    "не найден", userId));
        }
        return userStorage.get(userId);
    }

    public User saveUser(User user) {
        userStorage.save(user);
        return user;
    }

    public User updateUser(User user) {
        User updatedUser = userStorage.update(user);
        if (Objects.nonNull(updatedUser)) {
            return user;
        }
        throw new NotFoundException(String.format("Пользователь с id=%x" +
                " не найден", user.getId()));
    }

    public void addFriends(Long userId, Long friendId) {
        User userLoaded = userStorage.get(userId);
        User friendLoaded = userStorage.get(friendId);
        if (Objects.isNull(userLoaded)) {
            throw new NotFoundException(String.format("Пользователь с id=%x " +
                    "не найден", userId));
        } else if (Objects.isNull(friendLoaded)) {
            throw new NotFoundException(String.format("Пользователь с id=%x " +
                    "не найден", friendId));
        } else {
            Set<Long> userFriendSet = userLoaded.getFriendIds();
            userFriendSet.add(friendId);
            userStorage.get(userId).setFriendIds(userFriendSet);
            Set<Long> friendFriendSet = friendLoaded.getFriendIds();
            friendFriendSet.add(userId);
            userStorage.get(friendId).setFriendIds(friendFriendSet);
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        User userLoaded = userStorage.get(userId);
        User friendLoaded = userStorage.get(friendId);
        if (Objects.isNull(userLoaded)) {
            throw new NotFoundException(String.format("Пользователь с id=%x " +
                    "не найден", userId));
        } else if (Objects.isNull(friendLoaded)) {
            throw new NotFoundException(String.format("Пользователь с id=%x " +
                    "не найден", friendId));
        } else {
            Set<Long> userFriendSet = userLoaded.getFriendIds();
            userFriendSet.remove(friendId);
            userStorage.get(userId).setFriendIds(userFriendSet);
            Set<Long> friendFriendsSet = friendLoaded.getFriendIds();
            userFriendSet.remove(friendId);
            userStorage.get(userId).setFriendIds(userFriendSet);
        }
    }

    public List<User> getFriendList(Long userId) {
        User userLoaded = userStorage.get(userId);
        if (Objects.nonNull(userLoaded)) {
            return userLoaded.getFriendIds().stream()
                    .map(id -> userStorage.get(id))
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException(String.format("Пользователь с id=%x " +
                    "не найден", userId));
        }
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        User userLoaded = userStorage.get(userId);
        User friendLoaded = userStorage.get(friendId);
        if (Objects.isNull(userLoaded)) {
            throw new NotFoundException(String.format("Пользователь с id=%x " +
                    "не найден", userId));
        } else if (Objects.isNull(friendLoaded)) {
            throw new NotFoundException(String.format("Пользователь с id=%x " +
                    "не найден", friendId));
        } else {
            Set<Long> userFriendsSet = new HashSet<>(userLoaded.getFriendIds());
            userFriendsSet.retainAll(friendLoaded.getFriendIds());
            return userFriendsSet.stream()
                    .map(id -> userStorage.get(id))
                    .collect(Collectors.toList());
        }
    }

    public void clearMapForTests() {
        userStorage.clear();
    }
}