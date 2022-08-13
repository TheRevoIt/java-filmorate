package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendsStorage {
    void addFriends(Friendship friendship);

    void deleteFriend(Friendship friendship);

    List<Long> getFriendList(Long userId);

    List<Long> getCommonFriends(Friendship friendship);
}
