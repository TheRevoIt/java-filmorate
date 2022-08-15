package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {
    void addFriends(Friendship friendship);

    void deleteFriend(Friendship friendship);

    List<User> getFriendList(Long userId);

    List<User> getCommonFriends(Friendship friendship);
}
