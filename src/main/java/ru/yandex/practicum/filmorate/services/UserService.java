package ru.yandex.practicum.filmorate.services;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public UserService(UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public List<User> getAllEntries() {
        return userStorage.getAllEntries();
    }

    public User getUser(Long userId) {
        return userStorage.get(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь" +
                " с id=%x не найден", userId)));
    }

    public User saveUser(User user) {
        return userStorage.save(user);
    }

    public User updateUser(User user) {
        User updatedUser = userStorage.update(user);
        if (Objects.nonNull(updatedUser)) {
            return user;
        }
        throw new NotFoundException(String.format("Пользователь с id=%x" +
                " не найден", user.getId()));
    }

    public void deleteUser(Long userId) {
        getUser(userId);
        userStorage.delete(userId);
    }

    public void addFriends(Long userId, Long friendId) {
        getUser(userId);
        getUser(friendId);
        friendsStorage.addFriends(new Friendship(userId, friendId));
    }

    public void deleteFriend(Long userId, Long friendId) {
        getUser(userId);
        getUser(friendId);
        friendsStorage.deleteFriend(new Friendship(userId, friendId));
    }

    public List<User> getFriendList(Long userId) {
        getUser(userId);
        return friendsStorage.getFriendList(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        getUser(userId);
        getUser(friendId);
        return friendsStorage.getCommonFriends(new Friendship(userId, friendId));
    }

    public void clearMapForTests() {
        userStorage.clear();
    }
}