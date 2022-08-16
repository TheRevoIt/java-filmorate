package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        log.info("Список пользователей получен");
        return userService.getAllEntries();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        log.info("Пользователь получен");
        return userService.getUser(userId);
    }

    @PostMapping
    public User saveUser(@Valid @RequestBody User user) {
        User saved = userService.saveUser(user);
        log.info("Пользователь добавлен");
        return saved;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        User updated = userService.updateUser(user);
        log.info("Пользователь обновлен");
        return updated;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriends(@PathVariable Long userId,
                           @PathVariable Long friendId) {
        userService.addFriends(userId, friendId);
        log.info(String.format("Пользователи id=%x и id=%x " +
                "теперь друзья", userId, friendId));
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFriends(@PathVariable Long userId,
                              @PathVariable Long friendId) {
        log.info(String.format("Пользователи id=%x и id=%x " +
                "больше не друзья", userId, friendId));
        userService.deleteFriend(userId, friendId);
        return userService.getUser(userId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriendsList(@PathVariable Long userId) {
        List<User> friendsList = userService.getFriendList(userId);
        log.info(String.format("Список друзей пользователя id=%x получен", userId));
        return friendsList;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsList(@PathVariable Long id,
                                           @PathVariable Long otherId) {
        List<User> commonFriendsList = userService.getCommonFriends(id, otherId);
        log.info(String.format("Получен список общих друзей для пользователей id=%x " +
                "и id=%x", id, otherId));
        return commonFriendsList;
    }

    @GetMapping("/clear")
    public void clearAfterTest() {
        userService.clearMapForTests();
    }
}