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
        return userStorage.get(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь" +
                " с id=%x не найден", userId)));
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
        User userLoaded = getUser(userId);
        User friendLoaded = userStorage.get(friendId).orElseThrow(() -> new NotFoundException(String.format("Пользователь" +
                " с id=%x не найден", friendId)));
        Set<Long> userFriendSet = userLoaded.getFriendIds();
        userFriendSet.add(friendId);
        Set<Long> friendFriendSet = friendLoaded.getFriendIds();
        friendFriendSet.add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User userLoaded = getUser(userId);
        User friendLoaded = userStorage.get(friendId).orElseThrow(() -> new NotFoundException(String.format("Пользователь" +
                " с id=%x не найден", friendId)));
        Set<Long> userFriends= userLoaded.getFriendIds();
        userFriends.remove(friendId);
        Set<Long> friendFriends = friendLoaded.getFriendIds();
        friendFriends.remove(friendId);
    }

    public List<User> getFriendList(Long userId) {
        User userLoaded = getUser(userId);
        return userLoaded.getFriendIds().stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        User userLoaded = getUser(userId);
        User friendLoaded = userStorage.get(friendId).orElseThrow(() -> new NotFoundException(String.format("Пользователь" +
                " с id=%x не найден", friendId)));
        Set<Long> userFriendsSet = new HashSet<>(userLoaded.getFriendIds());
        userFriendsSet.retainAll(friendLoaded.getFriendIds());
        return userFriendsSet.stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    public void clearMapForTests() {
        userStorage.clear();
    }
}